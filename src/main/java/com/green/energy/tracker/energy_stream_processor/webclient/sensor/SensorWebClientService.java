package com.green.energy.tracker.energy_stream_processor.webclient.sensor;

import com.green.energy.tracker.energy_stream_processor.webclient.user.UserManagementWebClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("SensorWebClientServiceV1")
@RequiredArgsConstructor
public class SensorWebClientService {

    private final SensorWebClient sensorWebClient;

    @CircuitBreaker(name = "cb-site-sensor-management", fallbackMethod = "findByCodeFallback")
    public Long findByCode(String code) {
        return sensorWebClient.findByCode(code);
    }

    public void findByCodeFallback(String code, Throwable cause){
        String detailedMessage = String.format(
                "Sensor management service is currently unavailable. Unable to retrieve sensor for code '%s'. Cause: %s",
                code,
                cause.getMessage()
        );
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, detailedMessage);
    }
}
