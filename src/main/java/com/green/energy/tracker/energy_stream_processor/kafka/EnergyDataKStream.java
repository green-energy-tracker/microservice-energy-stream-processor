package com.green.energy.tracker.energy_stream_processor.kafka;

import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.model.EnergyDataIngest;
import com.green.energy.tracker.energy_stream_processor.model.EnrichEnergyDataEventResult;
import com.green.energy.tracker.energy_stream_processor.service.EnrichEnergyDataEventResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
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
    @Value("${spring.kafka.topic.data-energy-events-dlt}")
    private String energyDataEventsTopicDlt;
    private final EnrichEnergyDataEventResultService enrichEnergyDataEventResultService;



    @Bean
    public KStream<String, EnergyDataIngest> invoiceKStream(StreamsBuilder streamsBuilder, Serde<EnergyDataIngest> serdeEnergyDataIngest,
                                                           Serde<EnergyDataEvent> serdeEnergyDataEvent, Serde<DltRecord> serdeDltRecord) {

        var kStream = streamsBuilder.stream(energyDataIngestTopic,Consumed.with(Serdes.String(),serdeEnergyDataIngest));

        var kStreamEnrichEnergyDataEventResult = kStream
                .peek((key,energyDataEvent)-> log.info("Map energy data event: key: {} - value: {}",key,energyDataEvent))
                .mapValues(enrichEnergyDataEventResultService::buildEnergyDataEvent);

        kStreamEnrichEnergyDataEventResult.filter((key,enrichEnergyDataEventResult)-> enrichEnergyDataEventResult.isSuccess())
                .mapValues(EnrichEnergyDataEventResult::getData)
                .peek((key,energyDataEvent)-> log.info("Publishing to topic {} energy data event: key: {} - value: {}",energyDataEventsTopic,key,energyDataEvent))
                .to(energyDataEventsTopic, Produced.with(Serdes.String(),serdeEnergyDataEvent));

        kStreamEnrichEnergyDataEventResult.filter((key,enrichEnergyDataEventResult)-> !enrichEnergyDataEventResult.isSuccess())
                .mapValues(EnrichEnergyDataEventResult::getDltRecord)
                .peek((key,dltRecord)-> log.info("Publishing to topic {} dlt record: key: {} - value: {}",energyDataEventsTopicDlt,key,dltRecord))
                .to(energyDataEventsTopicDlt, Produced.with(Serdes.String(),serdeDltRecord));

        return kStream;
    }
}
