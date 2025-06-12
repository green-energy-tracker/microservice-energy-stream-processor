package com.green.energy.tracker.energy_stream_processor.webclient.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SiteWebClientConfigTest {

    @InjectMocks
    private SiteWebClientConfig siteWebClientConfig;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(siteWebClientConfig,"clientSiteSensorManagementUrl", "url");
        ReflectionTestUtils.setField(siteWebClientConfig,"clientSiteSensorManagementVersion", "1");
    }

    @Test
    void siteServiceClientTest(){
        var webClient = siteWebClientConfig.siteServiceClient();
        assertNotNull(webClient);
        assertEquals(SiteWebClient.class, webClient.getClass().getInterfaces()[0]);
    }
}
