package com.joker.entity;

import org.msgpack.annotation.Message;

/**
 * Created by joker on 2017/12/7.
 */
@Message
public class Provider {

    @NonEmpty
    private Node node;
    @NonEmpty
    private String serviceName;

    @NonEmpty
    private int a;


    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
