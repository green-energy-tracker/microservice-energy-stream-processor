package com.green.energy.tracker.energy_stream_processor.webclient.site;

import com.green.energy.tracker.configuration.domain.event.Site;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("SiteWebClientServiceV1")
@RequiredArgsConstructor
@Slf4j
public class SiteWebClientService {

    private final SiteWebClient siteWebClient;

    @CircuitBreaker(name = "cb-site-sensor-management", fallbackMethod = "findBySensorIdFallback")
    public Site findBySensorId(Long sensorId) {
        log.info("Http Request Site Microservice: GET findBySensorId, PARAMS: sensorId = {}",sensorId);
        return siteWebClient.findBySensorId(sensorId);
    }

    public void findBySensorIdFallback(Long sensorId, Throwable cause){
        String detailedMessage = "Site management service is currently unavailable. Unable to retrieve site for sensor id '%s'. Cause: %s";
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, String.format(detailedMessage,sensorId,cause.getMessage()));
    }
}
