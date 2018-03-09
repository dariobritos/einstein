package org.proygrad.einstein.rest.client;

import org.proygrad.einstein.api.ScenarioTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TuringClient {

    private static String DIR_TURING = "http://localhost:9040";

    private static String SCENARIO = DIR_TURING + "/scenario/";
    private static String PATCH_OUTPUT =  "/output";


    @Autowired
    private RestTemplate restTemplate;


    public ScenarioTO getScenario(String id) {
        return restTemplate.getForEntity(SCENARIO+id,ScenarioTO.class).getBody();
    }

    public String sendResult(ScenarioTO result) {
        return restTemplate.patchForObject( SCENARIO + result.getId() + PATCH_OUTPUT, (Object) result, String.class,result.getId() );
    }

}
