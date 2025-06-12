package com.green.energy.tracker.energy_stream_processor.kafka;

import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.model.EnergyDataIngest;
import com.green.energy.tracker.energy_stream_processor.service.EnrichEnergyDataEventResultService;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class EnergyDataKStreamTest {
    @Mock
    private EnrichEnergyDataEventResultService enrichEnergyDataEventResultService;
    @InjectMocks
    private EnergyDataKStream energyDataKStream;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(energyDataKStream,"energyDataIngestTopic", "test-topic");
        ReflectionTestUtils.setField(energyDataKStream,"energyDataEventsTopic", "test-topic");
        ReflectionTestUtils.setField(energyDataKStream,"energyDataEventsTopicDlt", "test-topic");
    }

    @Test
    void invoiceKStreamTest(){
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        Serde<EnergyDataIngest> energyDataIngestSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(EnergyDataIngest.class));
        Serde<EnergyDataEvent> energyDataEventSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(EnergyDataEvent.class));
        Serde<DltRecord> dltRecordSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(DltRecord.class));
        var result = energyDataKStream.invoiceKStream(streamsBuilder, energyDataIngestSerde, energyDataEventSerde, dltRecordSerde);
        assertNotNull(result);
    }
}
