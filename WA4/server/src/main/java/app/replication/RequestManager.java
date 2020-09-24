package app.replication;

import app.models.Amount;
import app.models.Transaction;
import app.models.User;
import app.replication.messages.requests.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestManager {

    public byte[] createUser(User user) {
        try {
            CreateUserRequestMessage message = new CreateUserRequestMessage(user.getUsername(), user.getPassword(),user.getCertificate());
            Request request = new Request(RequestType.CREATE_USER, message);
            return request.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] getUser(String username) {
        try {
            GetUserRequestMessage message = new GetUserRequestMessage(username);
            Request request = new Request(RequestType.GET_USER, message);
            return request.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] deposit(PermissionProof proof, float amount) {
        try {
            DepositRequestMessage message = new DepositRequestMessage(proof.getUsername(), amount);
            Request request = new Request(RequestType.DEPOSIT, message,proof);
            return request.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }



    public byte[] balance(PermissionProof proof) {
        try {
            BalanceRequestMessage message = new BalanceRequestMessage(proof.getUsername());
            Request request = new Request(RequestType.BALANCE, message,proof);
            return request.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] transfer(PermissionProof proof,Transaction transaction) {
        try {
            transaction.setId(UUID.randomUUID());
            TransferRequestMessage message = new TransferRequestMessage(transaction);
            Request request = new Request(RequestType.TRANSFER, message, proof);
            return request.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] ledger(PermissionProof proof, String username) {
        try {
            LedgerRequestMessage message = new LedgerRequestMessage(username);
            Request request = new Request(RequestType.LEDGER, message, proof);
            return request.serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}
