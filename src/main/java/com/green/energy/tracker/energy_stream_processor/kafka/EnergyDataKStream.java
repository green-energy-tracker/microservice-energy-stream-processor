package com.green.energy.tracker.energy_stream_processor.kafka;

import com.green.energy.tracker.energy_stream_processor.model.EnergyData;
import com.green.energy.tracker.energy_stream_processor.util.CustomSerdes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
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

    @Bean
    public KStream<String, EnergyData> invoiceKStream(StreamsBuilder streamsBuilder) {
        KStream<String, EnergyData> kStream = streamsBuilder.stream(energyDataIngestTopic, Consumed.with(Serdes.String(),customSerdes.topicEnergyDataIngest()));
        kStream.foreach((k,v)-> log.info("consumed energy data event: {}",v));
        return kStream;

    }
}
