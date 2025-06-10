package com.green.energy.tracker.energy_stream_processor.webclient.user;

import com.green.energy.tracker.configuration.domain.event.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service("UserManagementWebClientServiceV1")
@RequiredArgsConstructor
public class UserManagementWebClientService {

    private final UserManagementWebClient userManagementWebClient;

    @CircuitBreaker(name = "cb-user-management", fallbackMethod = "findByIdFallback")
    public User getUserById(Long id) {
        return userManagementWebClient.getUserById(id);
    }

    public void findByIdFallback(Long id, Throwable cause){
        String detailedMessage = String.format(
                "User management service is currently unavailable. Unable to retrieve user for user id '%s'. Cause: %s",
                id,
                cause.getMessage()
        );
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, detailedMessage);
    }
}
