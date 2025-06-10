package com.green.energy.tracker.energy_stream_processor.webclient.sensor;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SensorWebClient {
    @GetExchange("/findIdByUsername")
    Long findUserIdByUsername(@RequestParam String username);
}
