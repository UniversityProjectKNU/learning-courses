package com.nazar.grynko.learningcourses.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.nazar.grynko.learningcourses")
public class MvcConfiguration {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
