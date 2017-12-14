package com.joker.entity;

import com.joker.agreement.entity.Message;
import com.joker.agreement.entity.MessageType;
import com.joker.container.ChannelContainer;
import com.joker.dto.CustomerDTO;
import com.joker.utils.MessagePackageFactory;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 消费者
 * Created by joker on 2017/12/7.
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public class Customer implements Observer {

    public Customer(CustomerDTO customerDTO) {
        this.node = customerDTO.getNode();
        this.serviceNames = customerDTO.getServiceNames();
    }

    public Customer() {
    }

    @NonEmpty
    private Node node;

    @NonEmpty
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


    /**
     * 响应变化
     * @param o
     * @param arg 数组 0：操作类型 1：Provider
     */
    public void update(Observable o, Object arg) {
        Object[] objects = (Object[])arg;
        //通知事件
        Provider provider = (Provider)objects[1];
        byte[] bytes = MessagePackageFactory.entityToBytes(provider);
        Message message = null;
        if (objects[0] == OperateType.ADD) {
            message = Message.messageResult(bytes, MessageType.Success.value(),"singleAdd");
        } else {
            message = Message.messageResult(bytes, MessageType.Success.value(),"singleDelete");
        }
        ChannelContainer.wideSend(node.getId(),message);
    }
}
