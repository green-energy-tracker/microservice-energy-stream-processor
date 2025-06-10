package com.green.energy.tracker.energy_stream_processor.util;

import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.model.EnergyData;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

@Service
public class CustomSerdes {
    public Serde<EnergyDataEvent> topicEnergyDataEvents() {
        JsonSerializer<EnergyDataEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<EnergyDataEvent> deserializer = new JsonDeserializer<>(EnergyDataEvent.class, false);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public Serde<EnergyData> topicEnergyDataIngest() {
        JsonSerializer<EnergyData> serializer = new JsonSerializer<>();
        JsonDeserializer<EnergyData> deserializer = new JsonDeserializer<>(EnergyData.class, false);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
