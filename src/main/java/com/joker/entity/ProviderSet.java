package com.joker.entity;

import com.joker.agreement.entity.Message;
import com.joker.agreement.entity.MessageType;
import com.joker.container.ChannelContainer;
import com.joker.dto.ProviderList;
import com.joker.utils.MessagePackageFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * 生产者Set
 * Created by joker on 2017/12/7.
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public class ProviderSet extends Observable {
    private final String serviceName;//服务名
    private final static int N_LOCKS = 16;//锁长度
    private final int maxProvider;//最大的provider数
    private final Object[] locks;//分段锁
    private final Provider[] providers;//提供者们

    /**
     * 计算hash值
     * @param key
     * @return
     */
    private final int hash(Object key) {
        return Math.abs(key.hashCode() % providers.length);
    }

    /**
     * 构造函数
     * @param maxProvider
     * @param serviceName
     */
    public ProviderSet(int maxProvider,String serviceName) {
        this.serviceName = serviceName;
        this.maxProvider = maxProvider;
        locks = new Object[N_LOCKS];
        for (int i = 0 ; i < locks.length ; i ++) {
            locks[i] = 1;
        }
        providers = new Provider[maxProvider];
    }

    /**
     * 添加生产者
     * @param key
     * @param provider
     */
    public void addProvider(Object key,Provider provider) {
        int hash = hash(key);
        synchronized (locks[hash % maxProvider]) {
            providers[hash] = provider;
        }
        //通知消费者
        Object[] objects = {OperateType.ADD.value(),provider};
        setChanged();
        notifyObservers(objects);

    }


    public Provider getProvider(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % maxProvider]) {
            return providers[hash];
        }
    }

    /**
     * 移除生产者
     * @param key
     * @return
     */
    public Provider removeProvider(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % maxProvider]) {
            Provider provider = providers[hash];
            providers[hash] = null;
            //通知消费者
            Object[] objects = {OperateType.DELETE.value(),provider};
            setChanged();
            notifyObservers(objects);
            return provider;
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    /**
     * 注册观察者
     * @param o
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        //注册时通知消费者
        Customer customer = (Customer)o;
        ProviderList list = new ProviderList(providers,serviceName);
        byte[] bytes = MessagePackageFactory.entityToBytes(list);
        Message message = Message.messageResult(bytes, MessageType.Success.value(),"serviceList");
        ChannelContainer.wideSend(customer.getNode().getId(),message);
    }
}
