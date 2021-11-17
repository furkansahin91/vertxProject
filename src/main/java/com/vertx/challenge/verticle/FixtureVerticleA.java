package com.vertx.challenge.verticle;

import com.google.gson.Gson;
import com.vertx.challenge.model.FixtureAModel;
import com.vertx.challenge.service.FixtureService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FixtureVerticleA extends AbstractVerticle {
    private HttpRequest<JsonObject> request;

    @Autowired
    private FixtureService fixtureService;

    @Override
    public void start() {
        request = WebClient.create(vertx)
                .get(7299, "localhost", "/source/a")
                .as(BodyCodec.jsonObject())
                .expect(ResponsePredicate.SC_OK);
        vertx.setPeriodic(10, id -> handleFixtureA());
    }

    private void handleFixtureA() {
        request.send(asyncResult -> {
            if (asyncResult.succeeded()) {
                FixtureAModel fixtureModel = getFixtureAModel(asyncResult);
                try {
                    fixtureService.validateAndSinkFixture(fixtureModel.getId(), fixtureModel.getStatus());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private FixtureAModel getFixtureAModel(io.vertx.core.AsyncResult<io.vertx.ext.web.client.HttpResponse<JsonObject>> asyncResult) {
        try {
            return new Gson().fromJson(String.valueOf(Optional.of(asyncResult.result().body()).orElse(null)), FixtureAModel.class);
        } catch (Exception e) {
            System.out.println("malformed fixtue!");
        }
        return null;
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new FixtureVerticleA());
    }
}
