package com.green.energy.tracker.energy_stream_processor.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class KafkaStreamsConfigTest {
    @Mock
    private KafkaProperties kafkaProperties;
    @InjectMocks
    private KafkaStreamsConfig kafkaStreamsConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaStreamsConfig, "bootstrapServers", "test");
        ReflectionTestUtils.setField(kafkaStreamsConfig, "applicationId", "test");
        ReflectionTestUtils.setField(kafkaStreamsConfig, "schemaRegistryUrl", "test");
    }

    @Test
    void kafkaStreamsConfigurationTest(){
        var kafkaStreamsConfiguration = kafkaStreamsConfig.kafkaStreamsConfiguration(kafkaProperties);
        assertNotNull(kafkaStreamsConfiguration);
    }

    @Test
    void kafkaStreamsCustomizerTest(){
        var kafkaStreamsCustomizer = kafkaStreamsConfig.kafkaStreamsCustomizer();
        assertNotNull(kafkaStreamsCustomizer);
    }
}
