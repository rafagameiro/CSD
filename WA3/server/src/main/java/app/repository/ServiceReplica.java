package app.repository;

import bftsmart.tom.MessageContext;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import app.exceptions.InsufficientBalanceException;
import app.exceptions.UserAlreadyExistsException;
import app.exceptions.UserNotFoundException;
import app.models.Transaction;
import app.models.User;
import app.models.Wallet;
import app.replication.messages.replies.*;
import app.replication.messages.requests.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

@Component
public class ServiceReplica extends DefaultSingleRecoverable {

    @Value("${server.bftsmart.replica-id}")
    private int replicaId;

    private UserRepository userRepository;
    private WalletRepository walletRepository;

    @Autowired
    public ServiceReplica(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @PostConstruct
    private void initializeReplica() {
        new bftsmart.tom.ServiceReplica(replicaId, this, this);
    }

    @Override
    public void installSnapshot(byte[] bytes) {

    }

    @Override
    public byte[] getSnapshot() {
        return new byte[0];
    }

    @Override
    public byte[] appExecuteOrdered(byte[] bytes, MessageContext messageContext) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                RequestType type = RequestType.values()[dis.readInt()];
                byte[] message = new byte[dis.readInt()];
                dis.read(message);

                switch (type) {
                    case CREATE_USER:
                        return executeCreateUser(message).serialize();
                    case GET_USER:
                        return executeGetUser(message).serialize();
                    case DEPOSIT:
                        return executeDeposit(message).serialize();
                    case BALANCE:
                        return executeBalance(message).serialize();
                    case TRANSFER:
                        return executeTransfer(message).serialize();
                    case LEDGER:
                        return executeLedger(message).serialize();
                    default:
                        return new Reply(ReplyType.SERVER_ERROR).serialize();
                }
            }
        } catch (IOException e) {
            return new Reply(ReplyType.SERVER_ERROR).serialize();
        } catch (UserAlreadyExistsException e) {
            return new Reply(ReplyType.CONFLICT).serialize();
        } catch (UserNotFoundException e) {
            return new Reply(ReplyType.NOT_FOUND).serialize();
        } catch (InsufficientBalanceException e) {
            return new Reply(ReplyType.INVALID_OP).serialize();
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] bytes, MessageContext messageContext) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                RequestType type = RequestType.values()[dis.readInt()];
                byte[] message = new byte[dis.readInt()];
                dis.read(message);

                switch (type) {
                    case GET_USER:
                        return executeGetUser(message).serialize();
                    case BALANCE:
                        return executeBalance(message).serialize();
                    case LEDGER:
                        return executeLedger(message).serialize();
                    default:
                        return new Reply(ReplyType.SERVER_ERROR).serialize();
                }
            }
        } catch (IOException e) {
            return new Reply(ReplyType.SERVER_ERROR).serialize();
        } catch (UserNotFoundException e) {
            return new Reply(ReplyType.NOT_FOUND).serialize();
        }
    }

    private Reply executeGetUser(byte[] message) throws UserNotFoundException, IOException {
        GetUserRequestMessage userRequestMessage = GetUserRequestMessage.deserialize(message);
        User user = userRepository.findByUsername(userRequestMessage.getUsername());
        return new Reply(ReplyType.OK, new GetUserReplyMessage(user));
    }

    private Reply executeCreateUser(byte[] message) throws IOException, UserAlreadyExistsException {
        CreateUserRequestMessage userRequestMessage = CreateUserRequestMessage.deserialize(message);
        User user = new User(userRequestMessage.getUsername(), userRequestMessage.getPassword());
        Wallet wallet = new Wallet(user.getUsername());
        userRepository.save(user);
        walletRepository.save(wallet);

        return new Reply(ReplyType.OK);
    }

    private Reply executeBalance(byte[] message) throws IOException, UserNotFoundException {
        BalanceRequestMessage balanceRequestMessage = BalanceRequestMessage.deserialize(message);
        Wallet wallet = walletRepository.findByUsername(balanceRequestMessage.getUsername());

        return new Reply(ReplyType.OK, new BalanceReplyMessage(wallet.getBalance()));
    }

    private Reply executeDeposit(byte[] message) throws IOException, UserNotFoundException {
        DepositRequestMessage deposit = DepositRequestMessage.deserialize(message);
        Wallet wallet = walletRepository.findByUsername(deposit.getUsername());
        wallet.deposit(deposit.getAmount());
        walletRepository.update(wallet);

        return new Reply(ReplyType.OK);
    }

    private Reply executeTransfer(byte[] message) throws IOException, UserNotFoundException, InsufficientBalanceException {
        TransferRequestMessage transfer = TransferRequestMessage.deserialize(message);

        Transaction transaction = transfer.getTransaction();
        Wallet from = walletRepository.findByUsername(transaction.getFrom());
        Wallet to = walletRepository.findByUsername(transfer.getTransaction().getTo());
        float amount = transaction.getAmount();
        if (!from.canWithdraw(amount)) {
            throw new InsufficientBalanceException();
        }

        from.withdraw(amount);
        to.deposit(amount);

        from.addToLedger(transfer.getTransaction());
        to.addToLedger(transfer.getTransaction());

        walletRepository.update(from);
        walletRepository.update(to);

        return new Reply(ReplyType.OK);
    }

    private Reply executeLedger(byte[] message) throws IOException, UserNotFoundException {
        LedgerRequestMessage ledgerRequestMessage = LedgerRequestMessage.deserialize(message);

        Set<Transaction> ledger;
        if (!ledgerRequestMessage.getUsername().isEmpty()) {
            Wallet wallet = walletRepository.findByUsername(ledgerRequestMessage.getUsername());
            ledger = wallet.getLedger();
        } else {
            ledger = walletRepository.getAllTransactions();
        }

        return new Reply(ReplyType.OK, new LedgerReplyMessage(ledger));
    }
}
