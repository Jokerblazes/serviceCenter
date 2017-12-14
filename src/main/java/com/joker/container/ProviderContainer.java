package com.joker.container;

import com.joker.entity.Provider;
import com.joker.entity.ProviderSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by joker on 2017/12/7.
 * 生产者容器类
 * @key String
 * @value Future<ProviderSet>
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public class ProviderContainer {
    private static final Logger logger = LoggerFactory.getLogger(ProviderContainer.class);
    private static final ProviderContainer container = new ProviderContainer();
    public static ProviderContainer getInstance() {
        return container;
    }

    private final ConcurrentHashMap<String,Future<ProviderSet>> providerMap = new ConcurrentHashMap<String, Future<ProviderSet>>();

    /**
     * 注册service
     * @param serviceName
     * @param key
     * @param provider
     * https://github.com/Jokerblazes/serviceCenter.git
     */
    public void registService(final String serviceName, final Object key, final Provider provider) {
        //1：判断是否存在对应的serviceName
        //2:如果存在则塞到对应的set中
        //3:如果不存在则创建一个新的set并设置到map中

        //通过Future将存入过程与放入map的过程分离，避免重复放入
        while (true) {
            Future<ProviderSet> future = providerMap.get(serviceName);
            Callable<ProviderSet> eval = new Callable<ProviderSet>() {
                public ProviderSet call() throws Exception {
                    ProviderSet providerSet = new ProviderSet(10,serviceName);
                    providerSet.addProvider(key,provider);
                    return providerSet;
                }
            };
            FutureTask<ProviderSet> futureTask = new FutureTask<ProviderSet>(eval);
            if (future == null) {
                future = futureTask;
                providerMap.put(serviceName,future);
                futureTask.run();
            }
            try {
                try {
                    ProviderSet set = future.get();
                    set.addProvider(key,provider);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    e.printStackTrace();
                }
                return;
            } catch (CancellationException e) {
                providerMap.remove(serviceName,future);
            }  catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 移除service
     * @param serviceName
     * @param key
     * https://github.com/Jokerblazes/serviceCenter.git
     */
    public void removeService(String serviceName,Object key) {
        Future<ProviderSet> future = providerMap.get(serviceName);
        try {
            ProviderSet providerSet = future.get();
            if (providerSet == null) {
                logger.error("删除键为"+key+"的"+serviceName+"失败");
                throw new RuntimeException("删除键为" + key + "的" + serviceName + "失败");
            }
            Provider provider = providerSet.removeProvider(key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ProviderSet getServiceSet(String serviceName) {
        Future<ProviderSet> future = providerMap.get(serviceName);
        if (future == null)
            return null;
        ProviderSet providerSet = null;
        try {
            providerSet = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return providerSet;
    }

}
