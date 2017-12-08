package com.joker.entity;

import com.joker.container.ChannelContainer;
import com.joker.dto.ProviderList;
import com.joker.server.entity.Message;
import com.joker.server.entity.MessageType;
import com.joker.utils.MessagePackageFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by joker on 2017/12/7.
 */
public class ProviderSet extends Observable {
    private final String serviceName;
    private final static int N_LOCKS = 16;
    private final int maxProvider;
    private final Object[] locks;
    private final Provider[] providers;

    private final int hash(Object key) {
        return Math.abs(key.hashCode() % providers.length);
    }

    public ProviderSet(int maxProvider,String serviceName) {
        this.serviceName = serviceName;
        this.maxProvider = maxProvider;
        locks = new Object[N_LOCKS];
        for (int i = 0 ; i < locks.length ; i ++) {
            locks[i] = 1;
        }
        providers = new Provider[maxProvider];
    }


    public void addProvider(Object key,Provider provider) {
        int hash = hash(key);
        synchronized (locks[hash % maxProvider]) {
            providers[hash] = provider;
        }
        setChanged();
        notifyObservers(provider);

    }

    public Provider getProvider(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % maxProvider]) {
            return providers[hash];
        }
    }

    public Provider removeProvider(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % maxProvider]) {
            Provider provider = providers[hash];
            providers[hash] = null;
            setChanged();
            notifyObservers(provider);
            return provider;
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
//        System.out.println(o);
        Customer customer = (Customer)o;
        ProviderList list = new ProviderList(providers,serviceName);
        byte[] bytes = MessagePackageFactory.entityToBytes(list);
        Message message = Message.messageResult(bytes, MessageType.Success.value(),"serviceList");
        ChannelContainer.wideSend(customer.getNode().getId(),message);
    }
}