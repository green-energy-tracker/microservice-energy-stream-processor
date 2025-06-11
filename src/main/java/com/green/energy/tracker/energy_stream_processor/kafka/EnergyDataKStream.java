package com.green.energy.tracker.energy_stream_processor.kafka;

import com.green.energy.tracker.configuration.domain.event.EnergyData;
import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.model.EnergyDataIngest;
import com.green.energy.tracker.energy_stream_processor.webclient.sensor.SensorWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.site.SiteWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.user.UserManagementWebClientService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnergyDataKStream {

    @Value("${spring.kafka.topic.data-energy-ingest}")
    private String energyDataIngestTopic;
    @Value("${spring.kafka.topic.data-energy-events}")
    private String energyDataEventsTopic;
    @Value("${spring.kafka.topic.data-energy-events-dlt}")
    private String energyDataEventsTopicDlt;
    private final SensorWebClientService sensorWebClientService;
    private final SiteWebClientService siteWebClientService;
    private final UserManagementWebClientService userManagementWebClientService;
    private final ModelMapper modelMapper;


    @Bean
    public KStream<String, EnergyDataIngest> invoiceKStream(StreamsBuilder streamsBuilder, Serde<EnergyDataIngest> serdeEnergyDataIngest,
                                                           Serde<EnergyDataEvent> serdeEnergyDataEvent, Serde<DltRecord> serdeDltRecord) {

        KStream<String, EnergyDataIngest> kStream = streamsBuilder.stream(energyDataIngestTopic,Consumed.with(Serdes.String(),serdeEnergyDataIngest));

        KStream<String, EnrichEnergyDataEventResult> kStreamEnrichEnergyDataEventResult = kStream
                .peek((key,energyDataEvent)-> log.info("Map energy data event: key: {} - value: {}",key,energyDataEvent))
                .mapValues(this::buildEnergyDataEvent);

        kStreamEnrichEnergyDataEventResult.filter((key,enrichEnergyDataEventResult)-> enrichEnergyDataEventResult.isSuccess())
                .mapValues(EnrichEnergyDataEventResult::getData)
                .peek((key,energyDataEvent)->
                        log.info("Publishing to topic {} energy data event: key: {} - value: {}",energyDataEventsTopic,key,energyDataEvent))
                .to(energyDataEventsTopic, Produced.with(Serdes.String(),serdeEnergyDataEvent));

        kStreamEnrichEnergyDataEventResult.filter((key,enrichEnergyDataEventResult)-> !enrichEnergyDataEventResult.isSuccess())
                .mapValues(EnrichEnergyDataEventResult::getDltRecord)
                .peek((key,dltRecord)->
                        log.info("Publishing to topic {} dlt record: key: {} - value: {}",energyDataEventsTopicDlt,key,dltRecord))
                .to(energyDataEventsTopicDlt, Produced.with(Serdes.String(),serdeDltRecord));

        return kStream;
    }

    private EnrichEnergyDataEventResult buildEnergyDataEvent(String key, EnergyDataIngest energyDataIngest) {
        try {
            var sensorEvent = sensorWebClientService.findByCode(energyDataIngest.getSensorCode());
            var siteEvent = siteWebClientService.findBySensorId(sensorEvent.getId());
            var userEvent = userManagementWebClientService.getUserById(siteEvent.getOwnerId());
            var energyDataEvent = modelMapper.map(energyDataIngest, EnergyData.class);
            var energyDataEnrich = EnergyDataEvent.newBuilder().setEnergyData(energyDataEvent).setSensor(sensorEvent).setSite(siteEvent).setUser(userEvent).build();
            return EnrichEnergyDataEventResult.builder().success(true).data(energyDataEnrich).build();
        } catch (Exception ex) {
            var causedBy = Objects.isNull(ex.getCause()) ? "" : ex.getCause().getMessage();
            var dltRecord = DltRecord.builder().key(key).payload(energyDataIngest.toString()).error(ex.getMessage()).causedBy(causedBy).build();
            return EnrichEnergyDataEventResult.builder().success(false).dltRecord(dltRecord).build();
        }
    }

    @Builder
    @Data
    private static class EnrichEnergyDataEventResult {
        private boolean success;
        private EnergyDataEvent data;
        private DltRecord dltRecord;
    }
}
