package app.repository;

import app.exceptions.InsufficientBalanceException;
import app.exceptions.UserAlreadyExistsException;
import app.exceptions.UserNotFoundException;
import app.models.Transaction;
import app.models.User;
import app.models.Wallet;
import app.replication.messages.replies.*;
import app.replication.messages.requests.*;
import app.service.SmartContractService;
import bftsmart.reconfiguration.util.RSAKeyLoader;
import bftsmart.tom.MessageContext;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import bftsmart.tom.util.TOMUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.PrivateKey;
import java.util.Set;

@Component
public class ServiceReplica extends DefaultSingleRecoverable {

    @Value("${server.bftsmart.replica-id}")
    private int replicaId;

    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private SmartContractService smartContractService;

    @Autowired
    public ServiceReplica(UserRepository userRepository, WalletRepository walletRepository, SmartContractService smartContractService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.smartContractService = smartContractService;
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
                byte[] proof = new byte[dis.readInt()];
                dis.read(proof);

                switch (type) {
                    case CREATE_USER:
                        return sign(executeCreateUser(message).serialize());
                    case GET_USER:
                        return executeGetUser(message).serialize();
                    case DEPOSIT:
                        return sign(executeDeposit(message, proof).serialize());
                    case BALANCE:
                        return sign(executeBalance(message, proof).serialize());
                    case TRANSFER:
                        return sign(executeTransfer(message, proof).serialize());
                    case LEDGER:
                        return sign(executeLedger(message, proof).serialize());
                    default:
                        return sign(new Reply(ReplyType.SERVER_ERROR).serialize());
                }
            }
        } catch (IOException e) {
            return sign(new Reply(ReplyType.SERVER_ERROR).serialize());
        } catch (UserAlreadyExistsException e) {
            return sign(new Reply(ReplyType.CONFLICT).serialize());
        } catch (UserNotFoundException e) {
            return sign(new Reply(ReplyType.NOT_FOUND).serialize());
        } catch (InsufficientBalanceException e) {
            return sign(new Reply(ReplyType.INVALID_OP).serialize());
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] bytes, MessageContext messageContext) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                RequestType type = RequestType.values()[dis.readInt()];
                byte[] message = new byte[dis.readInt()];
                dis.read(message);
                byte[] proof = new byte[dis.readInt()];
                dis.read(proof);

                switch (type) {
                    case GET_USER:
                        return executeGetUser(message).serialize();
                    case BALANCE:
                        return sign(executeBalance(message, proof).serialize());
                    case LEDGER:
                        return sign(executeLedger(message, proof).serialize());
                    default:
                        return sign(new Reply(ReplyType.SERVER_ERROR).serialize());
                }
            }
        } catch (IOException e) {
            return sign(new Reply(ReplyType.SERVER_ERROR).serialize());
        } catch (UserNotFoundException e) {
            return sign(new Reply(ReplyType.NOT_FOUND).serialize());
        }
    }

    private Reply executeGetUser(byte[] message) throws UserNotFoundException, IOException {
        GetUserRequestMessage userRequestMessage = GetUserRequestMessage.deserialize(message);
        User user = userRepository.findByUsername(userRequestMessage.getUsername());
        return new Reply(ReplyType.OK, new GetUserReplyMessage(user));
    }

    private Reply executeCreateUser(byte[] message) throws IOException, UserAlreadyExistsException {
        CreateUserRequestMessage userRequestMessage = CreateUserRequestMessage.deserialize(message);
        User user = new User(userRequestMessage.getUsername(), userRequestMessage.getPassword(), userRequestMessage.getCertificate());
        Wallet wallet = new Wallet(user.getUsername());
        userRepository.save(user);
        walletRepository.save(wallet);

        return new Reply(ReplyType.OK);
    }

    private Reply executeBalance(byte[] message, byte[] proof) throws IOException, UserNotFoundException {
        BalanceRequestMessage balanceRequestMessage = BalanceRequestMessage.deserialize(message);
        PermissionProof permissionProof = PermissionProof.deserialize(proof);
        if (smartContractService.execute(permissionProof, SmartContractService.OperationPermissions.BALANCE)) {
            Wallet wallet = walletRepository.findByUsername(balanceRequestMessage.getUsername());
            return new Reply(ReplyType.OK, new BalanceReplyMessage(wallet.getBalance()));
        }

        return new Reply(ReplyType.FORBIDDEN);
    }

    private Reply executeDeposit(byte[] message, byte[] proof) throws IOException, UserNotFoundException {
        DepositRequestMessage deposit = DepositRequestMessage.deserialize(message);
        PermissionProof permissionProof = PermissionProof.deserialize(proof);

        if (smartContractService.execute(permissionProof, SmartContractService.OperationPermissions.DEPOSIT)) {
            Wallet wallet = walletRepository.findByUsername(deposit.getUsername());
            wallet.deposit(deposit.getAmount());
            walletRepository.update(wallet);

            return new Reply(ReplyType.OK);
        }

        return new Reply(ReplyType.FORBIDDEN);
    }

    private Reply executeTransfer(byte[] message, byte[] proof) throws IOException, UserNotFoundException, InsufficientBalanceException {
        TransferRequestMessage transfer = TransferRequestMessage.deserialize(message);
        PermissionProof permissionProof = PermissionProof.deserialize(proof);

        if (smartContractService.execute(permissionProof, SmartContractService.OperationPermissions.TRANSFER)) {
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

        return new Reply(ReplyType.FORBIDDEN);
    }

    private Reply executeLedger(byte[] message, byte[] proof) throws IOException, UserNotFoundException {
        LedgerRequestMessage ledgerRequestMessage = LedgerRequestMessage.deserialize(message);
        PermissionProof permissionProof = PermissionProof.deserialize(proof);

        Set<Transaction> ledger;
        if (!ledgerRequestMessage.getUsername().isEmpty() && smartContractService.execute(permissionProof, SmartContractService.OperationPermissions.LEDGERCLIENT)) {
            Wallet wallet = walletRepository.findByUsername(ledgerRequestMessage.getUsername());
            ledger = wallet.getLedger();
        } else if (ledgerRequestMessage.getUsername().isEmpty() && smartContractService.execute(permissionProof, SmartContractService.OperationPermissions.LEDGERGLOBAL)) {
            ledger = walletRepository.getAllTransactions();
        } else {
            return new Reply(ReplyType.FORBIDDEN);
        }

        return new Reply(ReplyType.OK, new LedgerReplyMessage(ledger));
    }

    private byte[] sign(byte[] message) {
        try {
            RSAKeyLoader rsa = new RSAKeyLoader(replicaId, "", false, replicaContext.getStaticConfiguration().getSignatureAlgorithm());

            byte[] hash = TOMUtil.computeHash(message);
            PrivateKey key = rsa.loadPrivateKey();
            byte[] hashSigned = TOMUtil.signMessage(key, hash);

            int size = message.length + hash.length + hashSigned.length + Integer.BYTES * 3;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(size)) {
                try (DataOutputStream dos = new DataOutputStream(bos)) {
                    dos.writeInt(message.length);
                    dos.write(message);
                    dos.writeInt(hash.length);
                    dos.write(hash);
                    dos.writeInt(hashSigned.length);
                    dos.write(hashSigned);
                    return bos.toByteArray();
                }
            }
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
