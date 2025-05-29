package com.example.movie.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }

    @Bean
    public DozerBeanMapper dozerMapper() {
        return (DozerBeanMapper) DozerBeanMapperBuilder.buildDefault();
    }

}

