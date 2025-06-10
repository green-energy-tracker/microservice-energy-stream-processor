package com.green.energy.tracker.energy_stream_processor.webclient.site;

import com.green.energy.tracker.configuration.domain.event.Sensor;
import com.green.energy.tracker.configuration.domain.event.Site;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("SiteWebClientServiceV1")
@RequiredArgsConstructor
public class SiteWebClientService {

    private final SiteWebClient siteWebClient;

    @CircuitBreaker(name = "cb-site-sensor-management", fallbackMethod = "findBySensorIdFallback")
    public Site findBySensorId(Long sensorId) {
        return siteWebClient.findBySensorId(sensorId);
    }

    public void findBySensorIdFallback(Long sensorId, Throwable cause){
        String detailedMessage = String.format(
                "Site management service is currently unavailable. Unable to retrieve site for sensor id '%s'. Cause: %s",
                sensorId,
                cause.getMessage()
        );
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, detailedMessage);
    }
}
