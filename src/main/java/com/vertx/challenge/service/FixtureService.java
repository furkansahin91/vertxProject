package com.vertx.challenge.service;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.vertx.challenge.model.Kind;
import com.vertx.challenge.model.SinkRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FixtureService {
    private static final String DONE = "done";
    private static final String SINK_URL = "http://localhost:7299/sink/a";

    private HazelcastInstance hzInstance;
    private ISet<String> fixtureSet;
    private RestTemplate restTemplate;

    public FixtureService() {
        hzInstance = Hazelcast.newHazelcastInstance();
        fixtureSet = hzInstance.getSet("fixtureSet");
        restTemplate = new RestTemplate();
    }

    public void validateAndSinkFixture(String id, String status) throws InterruptedException {
        if (id == null && DONE.equals(status) && !fixtureSet.contains(DONE)) {
            fixtureSet.add(DONE);
        } else if (id != null) {
            synchronized (this) {
                {
                    if (fixtureSet.contains(id)) {
                        System.out.println("joined fixture found" + id);
                        //sink one record
                        fixtureSet.remove(id);
                        sinkRecord(id, Kind.JOINED);
                        System.out.println(id + "joined sink to endpoint");
                    } else {
                        fixtureSet.add(id);
                    }
                }
            }
        } else if (fixtureSet.contains(DONE) && fixtureSet.size() > 1) {
            //sink all remaining record as orphaned
            fixtureSet.stream().forEach(i -> System.out.println("orphaned record sinked " + i));
            fixtureSet.remove(DONE);
            fixtureSet.stream().forEach(i -> sinkRecord(i, Kind.ORPHANED));
            fixtureSet.clear();
        }
    }

    private void sinkRecord(String id, Kind kind) {
        SinkRequest request = new SinkRequest();
        request.setId(id);
        request.setKind(kind.toString());
        restTemplate.postForEntity(SINK_URL, request, SinkRequest.class);
    }

    public ISet<String> getFixtureSet() {
        return fixtureSet;
    }

    public void setFixtureSet(ISet<String> fixtureSet) {
        this.fixtureSet = fixtureSet;
    }
}
