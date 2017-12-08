package com.joker.dto;

import com.joker.entity.Provider;
import org.msgpack.annotation.Message;

/**
 * Created by joker on 2017/12/8.
 */

@Message
public class ProviderList {
    private final Provider[] providers;
    private final String serviceName;

    public ProviderList(Provider[] providers, String serviceName) {
        this.providers = providers;
        this.serviceName = serviceName;
    }

    public Provider[] getProviders() {
        return providers;
    }

    public String getServiceName() {
        return serviceName;
    }
}
