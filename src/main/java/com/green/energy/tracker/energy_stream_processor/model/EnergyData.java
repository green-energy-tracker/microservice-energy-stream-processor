package com.green.energy.tracker.energy_stream_processor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnergyData {
    private Long id;
    private Long sensorId;
    private Long timestamp;
    private String energyKwh;
    private Boolean qualityFlag;
}
