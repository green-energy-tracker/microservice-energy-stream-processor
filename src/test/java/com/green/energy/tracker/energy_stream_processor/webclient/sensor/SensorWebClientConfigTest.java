package com.green.energy.tracker.energy_stream_processor.webclient.sensor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SensorWebClientConfigTest {

    @InjectMocks
    private SensorWebClientConfig sensorWebClientConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sensorWebClientConfig,"clientSiteSensorManagementUrl", "url");
        ReflectionTestUtils.setField(sensorWebClientConfig,"clientSiteSensorManagementVersion", "1");
    }

    @Test
    void sensorServiceClientTest(){
        var webClient = sensorWebClientConfig.sensorServiceClient();
        assertNotNull(webClient);
        assertEquals(SensorWebClient.class, webClient.getClass().getInterfaces()[0]);
    }
}
