package com.joker.container;

import com.joker.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by joker on 2017/12/7.
 * 消费者容器
 * @key int
 * @value Customer
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public class CustomerContainer  {
    private final static Logger logger = LoggerFactory.getLogger(CustomerContainer.class);
    private final static CustomerContainer container = new CustomerContainer();

    public static CustomerContainer getInstance() {
        return container;
    }

    private ConcurrentMap<Integer,Customer> map = new ConcurrentHashMap<Integer, Customer>();



    public void put(Integer key,Customer customerSet) {
        map.putIfAbsent(key,customerSet);
        logger.info("将键为{}值为{}的数据放入消费者容器！",key,customerSet);
    }

    public void remove(Integer key) {
        map.remove(key);
        logger.info("将键为{}的消费者移除容器！",key);
    }
}
