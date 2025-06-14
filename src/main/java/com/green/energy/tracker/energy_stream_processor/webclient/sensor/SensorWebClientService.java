package com.green.energy.tracker.energy_stream_processor.webclient.sensor;

import com.green.energy.tracker.configuration.domain.event.Sensor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("SensorWebClientServiceV1")
@RequiredArgsConstructor
@Slf4j
public class SensorWebClientService {

    private final SensorWebClient sensorWebClient;

    @CircuitBreaker(name = "cb-site-sensor-management", fallbackMethod = "findByCodeFallback")
    public Sensor findByCode(String code) {
        log.info("Http Request Sensor Microservice: GET findByCode, PARAMS: code = {}",code);
        return sensorWebClient.findByCode(code);
    }

    public void findByCodeFallback(String code, Throwable cause){
        var detailedMessage = "Sensor management service is currently unavailable. Unable to retrieve sensor for code '%s'. Cause: %s";
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, String.format(detailedMessage,code,cause.getMessage()));
    }
}
