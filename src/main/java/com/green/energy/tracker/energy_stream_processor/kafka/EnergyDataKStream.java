package com.green.energy.tracker.energy_stream_processor.kafka;

import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.model.EnergyData;
import com.green.energy.tracker.energy_stream_processor.util.CustomSerdes;
import com.green.energy.tracker.energy_stream_processor.webclient.sensor.SensorWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.site.SiteWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.user.UserManagementWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnergyDataKStream {

    @Value("${spring.kafka.topic.data-energy-ingest}")
    private String energyDataIngestTopic;
    @Value("${spring.kafka.topic.data-energy-events}")
    private String energyDataEventsTopic;
    private final CustomSerdes customSerdes;
    private final SensorWebClientService sensorWebClientService;
    private final SiteWebClientService siteWebClientService;
    private final UserManagementWebClientService userManagementWebClientService;
    private final ModelMapper modelMapper;

    @Bean
    public KStream<String, EnergyData> invoiceKStream(StreamsBuilder streamsBuilder) {

        KStream<String, EnergyData> kStream = streamsBuilder.stream(energyDataIngestTopic, Consumed.with(Serdes.String(),customSerdes.topicEnergyDataIngest()));

        kStream.peek((key,energyData)-> log.info("consuming energy data: key: {} - value: {}",key,energyData))
                .mapValues(this::buildEnergyDataEvent)
                .peek((key,energyDataEvent)-> log.info("mapping energy data event: key: {} - value: {}",key,energyDataEvent));
        return kStream;
    }

    private EnergyDataEvent buildEnergyDataEvent(EnergyData energyData){
        var sensorEvent = sensorWebClientService.findByCode(energyData.getSensorCode());
        var siteEvent = siteWebClientService.findBySensorId(sensorEvent.getId());
        var userEvent = userManagementWebClientService.getUserById(siteEvent.getOwnerId());
        var energyDataEvent = modelMapper.map(energyData, com.green.energy.tracker.configuration.domain.event.EnergyData.class);
        return EnergyDataEvent.newBuilder().setEnergyData(energyDataEvent).setSensor(sensorEvent).setSite(siteEvent).setUser(userEvent).build();
    }
}
