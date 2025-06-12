package com.green.energy.tracker.energy_stream_processor.webclient.site;

import com.green.energy.tracker.configuration.domain.event.Site;
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
class SiteWebClientServiceTest {
    @Mock
    private Site siteMock;
    @Mock
    private SiteWebClient siteWebClient;
    @InjectMocks
    private SiteWebClientService siteWebClientService;

    @Test
    void findBySensorIdTest(){
        when(siteWebClient.findBySensorId(1L)).thenReturn(siteMock);
        var site = siteWebClientService.findBySensorId(1L);
        assertEquals(site,siteMock);
        verify(siteWebClient).findBySensorId(1L);
    }

    @Test
    void findBySensorIdFallbackTest(){
        RuntimeException cause = new RuntimeException("Timeout");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> siteWebClientService.findBySensorIdFallback(1L, cause));
        assertNotNull(exception.getReason());
        assertTrue(exception.getReason().contains("Site management service is currently unavailable"));
        assertTrue(exception.getReason().contains("Timeout"));
    }
}
