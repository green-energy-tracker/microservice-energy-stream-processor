package com.green.energy.tracker.energy_stream_processor.webclient.sensor;

import com.green.energy.tracker.configuration.domain.event.Sensor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SensorWebClient {
    @GetExchange("/findByCode")
    Sensor findByCode(@RequestParam String code);
}
