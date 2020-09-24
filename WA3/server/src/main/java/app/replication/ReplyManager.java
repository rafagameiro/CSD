package app.replication;

import app.exceptions.*;
import app.models.Amount;
import app.models.Transaction;
import app.replication.messages.replies.BalanceReplyMessage;
import app.replication.messages.replies.GetUserReplyMessage;
import app.replication.messages.replies.LedgerReplyMessage;
import app.replication.messages.replies.ReplyType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;

@Component
public class ReplyManager {

    public void processCreateUser(byte[] replyBytes) throws UserAlreadyExistsException, ServerErrorException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyBytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                switch (type) {
                    case OK:
                        return;
                    case CONFLICT:
                        throw new UserAlreadyExistsException();
                    default:
                        throw new ServerErrorException();
                }
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }

    public boolean processVerifyUser(byte[] replyBytes, String password) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyBytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                byte[] messageBytes = new byte[replyBytes.length - Integer.BYTES];
                dis.read(messageBytes);
                if (type == ReplyType.OK) {
                    GetUserReplyMessage message = GetUserReplyMessage.deserialize(messageBytes);
                    return message.getUser().getPassword().equals(password);
                }
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public Amount processBalance(byte[] replyBytes) throws UserNotFoundException, ServerErrorException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyBytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                byte[] amount = new byte[replyBytes.length - Integer.BYTES];
                dis.read(amount);
                switch (type) {
                    case OK:
                        return new Amount(BalanceReplyMessage.deserialize(amount).getAmount());
                    case NOT_FOUND:
                        throw new UserNotFoundException();
                    default:
                        throw new ServerErrorException();
                }
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }

    public void processDeposit(byte[] replyBytes) throws UserNotFoundException, ServerErrorException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyBytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                switch (type) {
                    case OK:
                        return;
                    case NOT_FOUND:
                        throw new UserNotFoundException();
                    default:
                        throw new ServerErrorException();
                }
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }

    public void processTransfer(byte[] replyBytes) throws InsufficientBalanceException, UserNotFoundException, ServerErrorException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyBytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                switch (type) {
                    case OK:
                        return;
                    case INVALID_OP:
                        throw new InsufficientBalanceException();
                    case NOT_FOUND:
                        throw new UserNotFoundException();
                    default:
                        throw new ServerErrorException();
                }
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }

    public Set<Transaction> processLedgerClient(byte[] replyBytes) throws RepositoryException {
        return processLedger(replyBytes, (type, ledger) -> {
            switch (type) {
                case OK:
                    return LedgerReplyMessage.deserialize(ledger).getLedger();
                case NOT_FOUND:
                    throw new UserNotFoundException();
                default:
                    throw new ServerErrorException();
            }

        });
    }

    public Set<Transaction> processLedgerGlobal(byte[] replyBytes) throws RepositoryException {
        return processLedger(replyBytes, (type, ledger) -> {
            switch (type) {
                case OK:
                    return LedgerReplyMessage.deserialize(ledger).getLedger();
                default:
                    throw new ServerErrorException();
            }
        });
    }

    @FunctionalInterface
    interface Function {
        Set<Transaction> apply(ReplyType type, byte[] ledger) throws RepositoryException, IOException;
    }

    private Set<Transaction> processLedger(byte[] replyBytes, Function function) throws RepositoryException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyBytes)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                byte[] ledger = new byte[replyBytes.length - Integer.BYTES];
                dis.read(ledger);
                return function.apply(type, ledger);
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }



}
