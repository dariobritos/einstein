package org.proygrad.einstein.rest.client;

import org.proygrad.einstein.api.ScenarioTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TuringClient {

    private static String DIR_TURING = "http://usuario-hp:9040";

    private static String GET_SCENARIO = DIR_TURING + "/scenario/{}";
    private static String PATCH_SCENARIO = DIR_TURING + "/scenario/{}/output";

    @Autowired
    private RestTemplate restTemplate;


    public ScenarioTO getScenario(String id) {
        return restTemplate.getForEntity(GET_SCENARIO,ScenarioTO.class, id).getBody();
    }

    public String sendResult(ScenarioTO result) {
        return restTemplate.patchForObject(PATCH_SCENARIO, result, String.class, result.getId());
    }

}
