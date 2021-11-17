package com.vertx.challenge.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Id {
    private String value;

    public String getValue() {
        return value;
    }

    @XmlAttribute
    public void setValue(String value) {
        this.value = value;
    }

}
