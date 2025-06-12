package com.green.energy.tracker.energy_stream_processor.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomSerdesConfigTest {

    @InjectMocks
    private CustomSerdesConfig customSerdesConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(customSerdesConfig, "schemaRegistryUrl", "test");
        ReflectionTestUtils.setField(customSerdesConfig, "autoRegisterSchemas", true);
    }

    @Test
    void serdeConfigTest(){
        var config = customSerdesConfig.serdeConfig();
        assertNotNull(config);
    }

    @Test
    void serdeEnergyDataEventTest(){
        var serdeEnergyDataEvent = customSerdesConfig.serdeEnergyDataEvent();
        assertNotNull(serdeEnergyDataEvent);
    }

    @Test
    void serdeEnergyDataIngestTest(){
        var serdeEnergyDataIngest = customSerdesConfig.serdeEnergyDataIngest();
        assertNotNull(serdeEnergyDataIngest);
    }

    @Test
    void serdeDltRecordTest(){
        var serdeDltRecord = customSerdesConfig.serdeDltRecord();
        assertNotNull(serdeDltRecord);
    }

}
