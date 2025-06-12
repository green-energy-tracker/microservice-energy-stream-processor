package com.green.energy.tracker.energy_stream_processor.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ModelMapperConfigTest {

    @InjectMocks
    private ModelMapperConfig modelMapperConfig;

    @Test
    void modelMapperBeanTest(){
        var modelMapperBean = modelMapperConfig.modelMapperBean();
        assertNotNull(modelMapperBean);
    }
}
