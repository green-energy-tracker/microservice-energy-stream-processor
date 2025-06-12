package com.green.energy.tracker.energy_stream_processor.webclient.sensor;

import com.green.energy.tracker.configuration.domain.event.Sensor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorWebClientServiceTest {
    @Mock
    private Sensor sensorMock;
    @Mock
    private SensorWebClient sensorWebClient;
    @InjectMocks
    private SensorWebClientService sensorWebClientService;

    @Test
    void findByCodeTest(){
        when(sensorWebClient.findByCode("test")).thenReturn(sensorMock);
        var sensor = sensorWebClientService.findByCode("test");
        assertEquals(sensor,sensorMock);
        verify(sensorWebClient).findByCode("test");
    }

    @Test
    void findByCodeFallbackTest(){
        RuntimeException cause = new RuntimeException("Timeout");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> sensorWebClientService.findByCodeFallback("test", cause));
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().contains("Sensor management service is currently unavailable"));
        assertTrue(exception.getReason().contains("Timeout"));
    }
}
