package com.green.energy.tracker.energy_stream_processor.service;

import com.green.energy.tracker.configuration.domain.event.EnergyData;
import com.green.energy.tracker.configuration.domain.event.Sensor;
import com.green.energy.tracker.configuration.domain.event.Site;
import com.green.energy.tracker.configuration.domain.event.User;
import com.green.energy.tracker.energy_stream_processor.kafka.DltRecord;
import com.green.energy.tracker.energy_stream_processor.model.EnergyDataIngest;
import com.green.energy.tracker.energy_stream_processor.model.EnrichEnergyDataEventResult;
import com.green.energy.tracker.energy_stream_processor.webclient.sensor.SensorWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.site.SiteWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.user.UserManagementWebClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrichEnergyDataEventResultServiceImplTest {
    @Mock
    private SensorWebClientService sensorWebClientServiceMock;
    @Mock
    private SiteWebClientService siteWebClientServiceMock;
    @Mock
    private UserManagementWebClientService userManagementWebClientServiceMock;
    @Mock
    private ModelMapper modelMapperMock;
    @Mock
    private EnergyDataIngest energyDataIngestMock;
    @Mock
    private EnergyData energyDataMock;
    @Mock
    private Sensor sensorMock;
    @Mock
    private Site siteMock;
    @Mock
    private User userMock;
    @InjectMocks
    private EnrichEnergyDataEventResultServiceImpl enrichEnergyDataEventResultService;

    @Test
    void buildEnergyDataEventTest_success(){
        when(energyDataIngestMock.getSensorCode()).thenReturn("test");
        when(sensorWebClientServiceMock.findByCode("test")).thenReturn(sensorMock);
        when(siteWebClientServiceMock.findBySensorId(anyLong())).thenReturn(siteMock);
        when(userManagementWebClientServiceMock.getUserById(anyLong())).thenReturn(userMock);
        when(modelMapperMock.map(energyDataIngestMock, EnergyData.class)).thenReturn(energyDataMock);
        var result = enrichEnergyDataEventResultService.buildEnergyDataEvent("test",energyDataIngestMock);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertNull(result.getDltRecord());
    }

    @Test
    void buildEnergyDataEventTest_exceptionBySensorWebClient(){
        RuntimeException ex = new RuntimeException("sensor web client exception");
        when(energyDataIngestMock.getSensorCode()).thenReturn("test");
        when(sensorWebClientServiceMock.findByCode("test")).thenThrow(ex);
        var result = enrichEnergyDataEventResultService.buildEnergyDataEvent("test",energyDataIngestMock);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertNotNull(result.getDltRecord());
    }


    @Test
    void buildEnergyDataEventTest_exceptionBySiteWebClient(){
        RuntimeException ex = new RuntimeException("sensor web client exception");
        when(energyDataIngestMock.getSensorCode()).thenReturn("test");
        when(sensorWebClientServiceMock.findByCode("test")).thenReturn(sensorMock);
        when(siteWebClientServiceMock.findBySensorId(anyLong())).thenThrow(ex);
        var result = enrichEnergyDataEventResultService.buildEnergyDataEvent("test",energyDataIngestMock);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertNotNull(result.getDltRecord());
    }

    @Test
    void buildEnergyDataEventTest_exceptionByUserWebClient(){
        RuntimeException ex = new RuntimeException("sensor web client exception");
        when(energyDataIngestMock.getSensorCode()).thenReturn("test");
        when(sensorWebClientServiceMock.findByCode("test")).thenReturn(sensorMock);
        when(siteWebClientServiceMock.findBySensorId(anyLong())).thenReturn(siteMock);
        when(userManagementWebClientServiceMock.getUserById(anyLong())).thenThrow(ex);
        var result = enrichEnergyDataEventResultService.buildEnergyDataEvent("test",energyDataIngestMock);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertNotNull(result.getDltRecord());
    }
}
