package com.green.energy.tracker.energy_stream_processor.webclient.user;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("UserManagementWebClientServiceV1")
@RequiredArgsConstructor
public class UserManagementWebClientService {

    private final UserManagementWebClient userManagementServiceClient;

    @CircuitBreaker(name = "cb-user-management", fallbackMethod = "findUserIdByUsernameFallback")
    public Object findUserIdByUsername(String username) {
        return userManagementServiceClient.findUserIdByUsername(username);
    }

    public void findUserIdByUsernameFallback(String username, Throwable cause){
        String detailedMessage = String.format(
                "User management service is currently unavailable. Unable to retrieve user ID for username '%s'. Cause: %s",
                username,
                cause.getMessage()
        );
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, detailedMessage);
    }
}
