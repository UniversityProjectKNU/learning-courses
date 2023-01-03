package com.nazar.grynko.learningcourses.config;

import com.nazar.grynko.learningcourses.json.JsonArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        JsonArgumentResolver jsonArgumentResolver = new JsonArgumentResolver();
        argumentResolvers.add(jsonArgumentResolver);
    }

}
