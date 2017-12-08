package com.joker.entity;

import com.joker.container.ChannelContainer;
import com.joker.dto.ProviderList;
import com.joker.server.entity.Message;
import com.joker.server.entity.MessageType;
import com.joker.utils.MessagePackageFactory;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by joker on 2017/12/7.
 */
public class Customer implements Observer {

    private Node node;

    private List<String> serviceNames;

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }


    public void update(Observable o, Object arg) {
        //通知事件
        Provider provider = (Provider)arg;
        byte[] bytes = MessagePackageFactory.entityToBytes(provider);
        Message message = Message.messageResult(bytes, MessageType.Success.value(),"singleService");
        ChannelContainer.wideSend(node.getId(),message);
    }
}
