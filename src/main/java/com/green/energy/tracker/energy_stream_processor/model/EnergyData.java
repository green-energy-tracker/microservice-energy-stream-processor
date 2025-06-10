package com.green.energy.tracker.energy_stream_processor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnergyData {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("sensor_code")
    private String sensorCode;
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("energy_kwh")
    private String energyKwh;
    @JsonProperty("quality_flag")
    private Boolean qualityFlag;
}
