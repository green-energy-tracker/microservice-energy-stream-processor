package com.green.energy.tracker.energy_stream_processor.service;

import com.green.energy.tracker.energy_stream_processor.model.EnergyDataIngest;
import com.green.energy.tracker.energy_stream_processor.model.EnrichEnergyDataEventResult;

public interface EnrichEnergyDataEventResultService {
    EnrichEnergyDataEventResult buildEnergyDataEvent(String key,EnergyDataIngest energyDataIngest);
}
