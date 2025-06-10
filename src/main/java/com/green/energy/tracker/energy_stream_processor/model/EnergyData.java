package com.green.energy.tracker.energy_stream_processor.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EnergyData {
    private Long id;
    private Long sensorId;
    private Long timestamp;
    private String energyKwh;
    private Boolean qualityFlag;
}
