package com.green.energy.tracker.energy_stream_processor.config;

import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.kafka.DltRecord;
import com.green.energy.tracker.energy_stream_processor.model.EnergyDataIngest;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomSerdesConfig {

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;
    @Value(value = "${auto.register.schemas:false}")
    private boolean autoRegisterSchemas;

    @Bean
    public Map<String, Object> serdeConfig(){
        Map<String, Object> serdeConfig = new HashMap<>();
        serdeConfig.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        serdeConfig.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, String.valueOf(autoRegisterSchemas));
        serdeConfig.put(KafkaAvroSerializerConfig.AVRO_REMOVE_JAVA_PROPS_CONFIG, true);
        serdeConfig.put("specific.avro.reader", true);
        return serdeConfig;
    }

    @Bean
    public Serde<EnergyDataEvent> serdeEnergyDataEvent() {
        final Serde<EnergyDataEvent> serde = new SpecificAvroSerde<>();
        serde.configure(serdeConfig(), false);
        return serde;
    }

    @Bean
    public Serde<EnergyDataIngest> serdeEnergyDataIngest() {
        JsonSerializer<EnergyDataIngest> serializer = new JsonSerializer<>();
        JsonDeserializer<EnergyDataIngest> deserializer = new JsonDeserializer<>(EnergyDataIngest.class, false);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    @Bean
    public Serde<DltRecord> serdeDltRecord() {
        JsonSerializer<DltRecord> serializer = new JsonSerializer<>();
        JsonDeserializer<DltRecord> deserializer = new JsonDeserializer<>(DltRecord.class, false);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
