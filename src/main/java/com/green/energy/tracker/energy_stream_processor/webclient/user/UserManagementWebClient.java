package com.green.energy.tracker.energy_stream_processor.webclient.user;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface UserManagementWebClient {
    @GetExchange("/findIdByUsername")
    Object findUserIdByUsername(@RequestParam String username);
}
