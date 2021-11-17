package com.vertx.challenge.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Msg {
    private Id id;
    private String done;

    public Id getId() {
        return id;
    }

    @XmlElement
    public void setId(Id id) {
        this.id = id;
    }

    public String getDone() {
        return done;
    }

    @XmlElement
    public void setDone(String done) {
        this.done = done;
    }
}
