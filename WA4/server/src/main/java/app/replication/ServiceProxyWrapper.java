package app.replication;

import app.models.QuorumReplyList;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.core.messages.TOMMessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ServiceProxyWrapper {

    @Value("${server.bftsmart.client-id}")
    private int clientId;

    private AsynchServiceProxy proxy;

    @PostConstruct
    private void createProxy() {
        this.proxy = new AsynchServiceProxy(clientId, null, null, null, null);
    }

    public byte[] invokedUnordered(byte[] request) {
        return proxy.invoke(request, TOMMessageType.UNORDERED_REQUEST);
    }

    public void invokeOrdered(byte[] request, AsyncRequestHandler<QuorumReplyList> handler) {
        CustomReplyListener listener = new CustomReplyListener(proxy, handler);
        proxy.invokeAsynchRequest(request, listener, TOMMessageType.ORDERED_REQUEST);
    }

    public void invokeUnordered(byte[] request, AsyncRequestHandler<QuorumReplyList> handler) {
        CustomReplyListener listener = new CustomReplyListener(proxy, handler);
        proxy.invokeAsynchRequest(request, listener, TOMMessageType.UNORDERED_REQUEST);
    }

    public void invokeUnorderedHash(byte[] request, AsyncRequestHandler<QuorumReplyList> handler) {
        CustomReplyListener listener = new CustomReplyListener(proxy, handler);
        proxy.invokeAsynchRequest(request, listener, TOMMessageType.UNORDERED_HASHED_REQUEST);
    }

}
