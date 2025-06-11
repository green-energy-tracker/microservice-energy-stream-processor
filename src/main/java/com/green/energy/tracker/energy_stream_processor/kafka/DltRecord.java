package com.green.energy.tracker.energy_stream_processor.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DltRecord {
    private String key;
    private String payload;
    private String error;
    private String causedBy;
}
