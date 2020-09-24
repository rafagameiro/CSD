package app.service;

import app.models.QuorumReplyList;
import app.models.Transaction;
import app.models.User;
import app.replication.ReplyManager;
import app.replication.RequestManager;
import app.replication.ServiceProxyWrapper;
import app.replication.messages.requests.PermissionProof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class WalletService {
    private ServiceProxyWrapper serviceProxy;
    private RequestManager requestManager;
    private ReplyManager replyManager;

    @Autowired
    public WalletService(ServiceProxyWrapper serviceProxy, RequestManager requestManager, ReplyManager replyManager) {
        this.serviceProxy = serviceProxy;
        this.requestManager = requestManager;
        this.replyManager = replyManager;
    }

    public void createUser(User user, Consumer<QuorumReplyList> onSuccess) {
        byte[] request = requestManager.createUser(user);
        serviceProxy.invokeOrdered(request, onSuccess::accept);
    }

    public boolean verifyUser(String username, String password) {
        byte[] request = requestManager.getUser(username);
        return replyManager.processVerifyUser(serviceProxy.invokedUnordered(request), password);
    }

    public void balance(PermissionProof proof, Consumer<QuorumReplyList> onSuccess) {
        byte[] request = requestManager.balance(proof);
        serviceProxy.invokeUnordered(request, onSuccess::accept);
    }

    public void deposit(PermissionProof proof, float amount, Consumer<QuorumReplyList> onSuccess) {
        byte[] request = requestManager.deposit(proof, amount);
        serviceProxy.invokeOrdered(request, onSuccess::accept);
    }

    public void transfer(PermissionProof proof, Transaction transaction, Consumer<QuorumReplyList> onSuccess) {
        byte[] request = requestManager.transfer(proof,transaction);
        serviceProxy.invokeOrdered(request, onSuccess::accept);
    }

    public void getLedger(PermissionProof proof, String username, Consumer<QuorumReplyList> onSuccess) {
        byte[] request = requestManager.ledger(proof,username);
        serviceProxy.invokeUnordered(request, onSuccess::accept);
    }

    public void getLedger(PermissionProof proof, Consumer<QuorumReplyList> onSuccess) {
        byte[] request = requestManager.ledger(proof,"");
        serviceProxy.invokeUnordered(request, onSuccess::accept);
    }

}
