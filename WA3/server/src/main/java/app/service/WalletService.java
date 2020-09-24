package app.service;

import app.exceptions.*;
import app.models.Amount;
import app.models.Transaction;
import app.models.User;
import app.replication.ReplyManager;
import app.replication.RequestManager;
import app.replication.ServiceProxyWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    public void createUser(User user) throws UserAlreadyExistsException, ServerErrorException {
        byte[] request = requestManager.createUser(user);
        replyManager.processCreateUser(serviceProxy.invokeOrdered(request));
    }

    public boolean verifyUser(String username, String password) {
        byte[] request = requestManager.getUser(username);
        return replyManager.processVerifyUser(serviceProxy.invokeUnordered(request), password);
    }

    public Amount balance(String username) throws UserNotFoundException, ServerErrorException {
        byte[] request = requestManager.balance(username);
        return replyManager.processBalance(serviceProxy.invokeUnordered(request));
    }

    public void deposit(String username, Amount amount) throws UserNotFoundException, ServerErrorException {
        byte[] request = requestManager.deposit(username, amount);
        replyManager.processDeposit(serviceProxy.invokeOrdered(request));
    }

    public void transfer(Transaction transaction) throws UserNotFoundException, InsufficientBalanceException, ServerErrorException {
        byte[] request = requestManager.transfer(transaction);
        replyManager.processTransfer(serviceProxy.invokeOrdered(request));
    }

    public Set<Transaction> getLedger(String username) throws RepositoryException {
        byte[] request = requestManager.ledger(username);
        return replyManager.processLedgerClient(serviceProxy.invokeUnordered(request));
    }

    public Set<Transaction> getLedger() throws RepositoryException {
        byte[] request = requestManager.ledger("");
        return replyManager.processLedgerGlobal(serviceProxy.invokeUnordered(request));
    }
}
