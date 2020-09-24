package app.replication;

import bftsmart.tom.ServiceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ServiceProxyWrapper {

    @Value("${server.bftsmart.client-id}")
    private int clientId;

    private ServiceProxy proxy;

    @PostConstruct
    private void createProxy() {
        this.proxy = new ServiceProxy(clientId);
    }

    public byte[] invokeOrdered(byte[] request) {
        return proxy.invokeOrdered(request);
    }

    public byte[] invokeUnordered(byte[] request) {
        return proxy.invokeUnordered(request);
    }
}
