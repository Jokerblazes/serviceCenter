package com.joker.container;

import com.joker.entity.Customer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by joker on 2017/12/7.
 */
public class CustomerContainer  {
    private final static CustomerContainer container = new CustomerContainer();

    public static CustomerContainer getInstance() {
        return container;
    }

    private ConcurrentMap<Integer,Customer> map = new ConcurrentHashMap<Integer, Customer>();


    public void put(Integer key,Customer customerSet) {
        map.putIfAbsent(key,customerSet);
    }

    public void remove(Integer key) {
        map.remove(key);
    }
}
