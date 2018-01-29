package org.proygrad.einstein.service.nontransactional;

import org.proygrad.einstein.api.ScenarioTO;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class TuringConfig {

    public ScenarioTO getScenario(UUID requestId) {
        // obtener con requestId el Scenario correspondiente.
        return null;
    }

    public void sendResult(ScenarioTO result) {
        // enviar resultado.
    }
}
