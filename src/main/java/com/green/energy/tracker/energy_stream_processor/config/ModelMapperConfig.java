package com.green.energy.tracker.energy_stream_processor.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapperBean(){
        return new ModelMapper();
    }

}
