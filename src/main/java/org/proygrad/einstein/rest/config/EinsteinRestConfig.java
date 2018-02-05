package org.proygrad.einstein.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EinsteinRestConfig {

    @Bean
    // @LoadBalanced
    public RestTemplate restTemplate(){

        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);

        restTemplate.setRequestFactory(requestFactory);

        return restTemplate;
    }
}
