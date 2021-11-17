package com.vertx.challenge;

import com.vertx.challenge.verticle.FixtureVerticleA;
import com.vertx.challenge.verticle.FixtureVerticleB;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@AllArgsConstructor
public class VertxChallengeApplication extends AbstractVerticle {

    private final FixtureVerticleA fixtureVerticleA;
    private final FixtureVerticleB fixtureVerticleB;

    public static void main(String[] args) {
        SpringApplication.run(VertxChallengeApplication.class, args);
    }

    @PostConstruct
    public void deployVerticle() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(fixtureVerticleB);
        vertx.deployVerticle(fixtureVerticleA);

    }
}
