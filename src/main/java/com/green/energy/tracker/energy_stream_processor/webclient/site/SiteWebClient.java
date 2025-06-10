package com.green.energy.tracker.energy_stream_processor.webclient.site;

import com.green.energy.tracker.configuration.domain.event.Sensor;
import com.green.energy.tracker.configuration.domain.event.Site;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SiteWebClient {
    @GetExchange("/findBySensor")
    Site findBySensor(@RequestParam Sensor sensor);
}
