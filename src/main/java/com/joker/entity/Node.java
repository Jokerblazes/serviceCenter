package com.joker.entity;


import org.msgpack.annotation.Message;

/**
 * 消费者、生产者公共类
 * Created by joker on 2017/12/7.
 * https://github.com/Jokerblazes/serviceCenter.git
 */
@Message
public class Node {
    @NonEmpty
    private int id;
    @NonEmpty
    private String ip;
    @NonEmpty
    private int port;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
