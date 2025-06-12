package com.green.energy.tracker.energy_stream_processor.webclient.user;

import com.green.energy.tracker.configuration.domain.event.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserManagementWebClientServiceTest {
    @Mock
    private User userMock;
    @Mock
    private UserManagementWebClient userManagementWebClient;
    @InjectMocks
    private UserManagementWebClientService userManagementWebClientService;

    @Test
    void getUserByIdTest(){
        when(userManagementWebClient.getUserById(1L)).thenReturn(userMock);
        var user = userManagementWebClientService.getUserById(1L);
        assertEquals(user,userMock);
        verify(userManagementWebClient).getUserById(1L);
    }

    @Test
    void getUserByIdFallbackTest(){
        RuntimeException cause = new RuntimeException("Timeout");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userManagementWebClientService.getUserByIdFallback(1L, cause));
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().contains("User management service is currently unavailable"));
        assertTrue(exception.getReason().contains("Timeout"));
    }
}
