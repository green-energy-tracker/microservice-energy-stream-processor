package com.green.energy.tracker.energy_stream_processor.service;

import com.green.energy.tracker.configuration.domain.event.EnergyData;
import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.kafka.DltRecord;
import com.green.energy.tracker.energy_stream_processor.model.EnergyDataIngest;
import com.green.energy.tracker.energy_stream_processor.model.EnrichEnergyDataEventResult;
import com.green.energy.tracker.energy_stream_processor.webclient.sensor.SensorWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.site.SiteWebClientService;
import com.green.energy.tracker.energy_stream_processor.webclient.user.UserManagementWebClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("EnrichEnergyDataEventResultServiceV1")
@RequiredArgsConstructor
public class EnrichEnergyDataEventResultServiceImpl implements EnrichEnergyDataEventResultService {

    private final SensorWebClientService sensorWebClientService;
    private final SiteWebClientService siteWebClientService;
    private final UserManagementWebClientService userManagementWebClientService;
    private final ModelMapper modelMapper;

    @Override
    public EnrichEnergyDataEventResult buildEnergyDataEvent(String key, EnergyDataIngest energyDataIngest) {
        try {
            var sensorEvent = sensorWebClientService.findByCode(energyDataIngest.getSensorCode());
            var siteEvent = siteWebClientService.findBySensorId(sensorEvent.getId());
            var userEvent = userManagementWebClientService.getUserById(siteEvent.getOwnerId());
            var energyDataEvent = modelMapper.map(energyDataIngest, EnergyData.class);
            var energyDataEnrich = EnergyDataEvent.newBuilder().setEnergyData(energyDataEvent).setSensor(sensorEvent).setSite(siteEvent).setUser(userEvent).build();
            return EnrichEnergyDataEventResult.builder().success(true).data(energyDataEnrich).build();
        } catch (Exception ex) {
            var causedBy = Objects.isNull(ex.getCause()) ? "" : ex.getCause().getMessage();
            var dltRecord = DltRecord.builder().key(key).payload(energyDataIngest.toString()).error(ex.getMessage()).causedBy(causedBy).build();
            return EnrichEnergyDataEventResult.builder().success(false).dltRecord(dltRecord).build();
        }
    }
}
