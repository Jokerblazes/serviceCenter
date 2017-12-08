package com.joker.entity;

/**
 * Created by joker on 2017/12/7.
 */
public class ChannelKey {
    private final int id;
    private final String serviceName;

    public ChannelKey(int id, String serviceName) {
        this.id = id;
        this.serviceName = serviceName;
    }

    public int getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

}
