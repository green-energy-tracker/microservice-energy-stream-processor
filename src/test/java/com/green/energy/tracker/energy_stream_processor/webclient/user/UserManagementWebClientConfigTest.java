package com.green.energy.tracker.energy_stream_processor.webclient.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserManagementWebClientConfigTest {

    @InjectMocks
    private UserManagementWebClientConfig userManagementWebClientConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userManagementWebClientConfig,"clientUserManagementUrl", "url");
        ReflectionTestUtils.setField(userManagementWebClientConfig,"clientUserManagementVersion", "1");
    }

    @Test
    void userManagementServiceClientTest(){
        var webClient = userManagementWebClientConfig.userManagementServiceClient();
        assertNotNull(webClient);
        assertEquals(UserManagementWebClient.class, webClient.getClass().getInterfaces()[0]);
    }
}
