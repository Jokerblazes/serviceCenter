package com.joker.container;

import com.joker.entity.Provider;
import com.joker.entity.ProviderSet;

import java.util.concurrent.*;

/**
 * Created by joker on 2017/12/7.
 */
public class ProviderContainer {
    private static final ProviderContainer container = new ProviderContainer();
    public static ProviderContainer getInstance() {
        return container;
    }

    private final ConcurrentHashMap<String,Future<ProviderSet>> providerMap = new ConcurrentHashMap<String, Future<ProviderSet>>();


    public void registService(final String serviceName, final Object key, final Provider provider) {
        //1：判断是否存在对应的serviceName
        //2:如果存在则塞到对应的set中
        //3:如果不存在则创建一个新的set并设置到map中


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

    public void removeService(String serviceName,Object key) {
        Future<ProviderSet> future = providerMap.get(serviceName);
        try {
            ProviderSet providerSet = future.get();
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
