package com.joker.entity;

import org.msgpack.annotation.Message;

/**
 * 生产者
 * Created by joker on 2017/12/7.
 * https://github.com/Jokerblazes/serviceCenter.git
 */
@Message
public class Provider {

    @NonEmpty
    private Node node;
    @NonEmpty
    private String serviceName;

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
