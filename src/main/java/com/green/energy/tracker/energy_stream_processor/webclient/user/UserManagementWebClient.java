package com.green.energy.tracker.energy_stream_processor.webclient.user;

import com.green.energy.tracker.configuration.domain.event.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface UserManagementWebClient {
    @GetExchange("/findById")
    User getUserById(@RequestParam Long id);
}
