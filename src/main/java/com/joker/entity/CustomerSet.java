//package com.joker.entity;
//
//import com.joker.container.ChannelContainer;
//import com.joker.server.entity.Message;
//
//import java.util.Observable;
//import java.util.Observer;
//
///**
// * Created by joker on 2017/12/7.
// */
//public class CustomerSet implements Observer {
//
//
//    private final static int N_LOCKS = 16;
//    private int maxCustomer;
//    private Object[] locks;
//    private Customer[] customers;
//
//    private final int hash(Object key) {
//        return Math.abs(key.hashCode() % customers.length);
//    }
//
//    public CustomerSet(int maxCustomer) {
//        this.maxCustomer = maxCustomer;
//        locks = new Object[N_LOCKS];
//        customers = new Customer[maxCustomer];
//    }
//
//
//    public void addCustomer(Object key,Customer customer) {
//        int hash = hash(key);
//        synchronized (locks[hash % maxCustomer]) {
//            customers[hash] = customer;
//        }
//    }
//
//    public Customer getCustomer(Object key) {
//        int hash = hash(key);
//        synchronized (locks[hash % maxCustomer]) {
//            return customers[hash];
//        }
//    }
//
//    public void removeCustomer(Object key) {
//        int hash = hash(key);
//        synchronized (locks[hash % maxCustomer]) {
//            customers[hash] = null;
//        }
//    }
//
//
//
//    public void update(Observable o, Object arg) {
////        Message message = Message.messageResult()
////        ChannelContainer.wideSend();
//    }
//
//
//}
