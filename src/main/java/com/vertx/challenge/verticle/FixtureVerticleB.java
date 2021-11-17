package com.vertx.challenge.verticle;


import com.vertx.challenge.model.Msg;
import com.vertx.challenge.service.FixtureService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Optional;

@Component
public class FixtureVerticleB extends AbstractVerticle {
    private HttpRequest<String> request;

    @Autowired
    private FixtureService fixtureService;

    @Override
    public void start() {
        request = WebClient.create(vertx)
                .get(7299, "localhost", "/source/b")
                .as(BodyCodec.string());
        vertx.setPeriodic(10, id -> fetchFixture());
    }

    private void fetchFixture() {
        request.send(asyncResult -> {
            if (asyncResult.succeeded()) {
                Msg fixtureModelB = getMsg(asyncResult);
                if (fixtureModelB != null) {
                    String id = fixtureModelB.getId() != null ? fixtureModelB.getId().getValue() : null;
                    try {
                        fixtureService.validateAndSinkFixture(id, fixtureModelB.getDone());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Msg getMsg(io.vertx.core.AsyncResult<io.vertx.ext.web.client.HttpResponse<String>> asyncResult) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Msg.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Msg) jaxbUnmarshaller.unmarshal(new StringReader(Optional.of(asyncResult.result().body()).orElse(null)));
        } catch (JAXBException e) {
            System.out.println("malformed fixture!");
        }
        return null;
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new FixtureVerticleB());
    }
}
