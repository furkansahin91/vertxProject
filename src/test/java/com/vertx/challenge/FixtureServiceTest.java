package com.vertx.challenge;

import com.vertx.challenge.service.FixtureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FixtureServiceTest {

    @InjectMocks
    FixtureService fixtureService;
    @Mock
    RestTemplate restTemplate;

    @Test
    public void validateAndSinkFixture_when_add() throws InterruptedException {
        fixtureService.validateAndSinkFixture("34564", "ok");
        Assertions.assertEquals(fixtureService.getFixtureSet().size(), 1);
    }

    @Test
    public void validateAndSinkFixture_when_ajoin() throws InterruptedException {

        when(restTemplate.postForEntity(anyString(),any(),any())).thenReturn(null);
        fixtureService.validateAndSinkFixture("34564", "ok");
        Assertions.assertEquals(fixtureService.getFixtureSet().size(), 0);
    }

    @Test
    public void validateAndSinkFixture_when_done() throws InterruptedException {
        fixtureService.validateAndSinkFixture("34565", "ok");
        fixtureService.validateAndSinkFixture(null, "done");
        Assertions.assertEquals(fixtureService.getFixtureSet().size(), 2);

        when(restTemplate.postForEntity(anyString(),any(),any())).thenReturn(null);
        fixtureService.validateAndSinkFixture(null, "done");
        Assertions.assertEquals(fixtureService.getFixtureSet().size(), 0);
    }
}
