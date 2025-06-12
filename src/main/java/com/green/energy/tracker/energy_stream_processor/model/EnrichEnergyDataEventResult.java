package com.green.energy.tracker.energy_stream_processor.model;

import com.green.energy.tracker.configuration.domain.event.EnergyDataEvent;
import com.green.energy.tracker.energy_stream_processor.kafka.DltRecord;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnrichEnergyDataEventResult {
    private boolean success;
    private EnergyDataEvent data;
    private DltRecord dltRecord;
}
