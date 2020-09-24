package app.commandresults;

import app.commandresults.replies.BalanceReplyMessage;
import app.commandresults.replies.LedgerReplyMessage;
import app.commandresults.replies.ReplyType;
import app.exceptions.*;
import app.models.*;
import app.utils.RSAKeyLoader;
import app.utils.TOMUtil;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

@Component
public class ReplyManager {

    public void processCreateUser(QuorumReplyList replyList, Configuration config) throws UserAlreadyExistsException, ServerErrorException, UnauthorizedException {
        if (!verifyQuorum(replyList, config)) {
            throw new ServerErrorException();
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(replyList.getMessage())) {
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

    private boolean verifyReply(HashSignaturePair pair) {
        RSAKeyLoader loader = RSAKeyLoader.getInstance();
        try {
            PublicKey key = loader.loadPublicKey(pair.getSenderId());
            if (TOMUtil.verifySignature(key, pair.getHash(), pair.getSignature())) {
                return true;
            }

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | CertificateException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Amount processBalance(QuorumReplyList replies, Configuration config) throws UserNotFoundException, ServerErrorException, UnauthorizedException {
        if (!verifyQuorum(replies, config)){
            throw new ServerErrorException();
        }
        byte[] reply = replies.getMessage();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(reply)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                byte[] amount = new byte[reply.length - Integer.BYTES];
                dis.read(amount);
                switch (type) {
                    case OK:
                        return new Amount(BalanceReplyMessage.deserialize(amount).getAmount());
                    case NOT_FOUND:
                        throw new UserNotFoundException();
                    case FORBIDDEN:
                        throw new UnauthorizedException();
                    default:
                        throw new ServerErrorException();
                }
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }

    public void processDeposit(QuorumReplyList replies, Configuration config) throws UserNotFoundException, ServerErrorException, UnauthorizedException {
        if (!verifyQuorum(replies, config)){
            throw new ServerErrorException();
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(replies.getMessage())) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                switch (type) {
                    case OK:
                        return;
                    case NOT_FOUND:
                        throw new UserNotFoundException();
                    case FORBIDDEN:
                        throw new UnauthorizedException();
                    default:
                        throw new ServerErrorException();
                }
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }

    public void processTransfer(QuorumReplyList replies, Configuration config) throws InsufficientBalanceException, UserNotFoundException, ServerErrorException, UnauthorizedException {
        if (!verifyQuorum(replies, config)){
            throw new ServerErrorException();
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(replies.getMessage())) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                ReplyType type = ReplyType.values()[dis.readInt()];
                switch (type) {
                    case OK:
                        return;
                    case INVALID_OP:
                        throw new InsufficientBalanceException();
                    case NOT_FOUND:
                        throw new UserNotFoundException();
                    case FORBIDDEN:
                        throw new UnauthorizedException();
                    default:
                        throw new ServerErrorException();
                }
            }
        } catch (IOException e) {
            throw new ServerErrorException();
        }
    }

    public Set<Transaction> processLedgerClient(QuorumReplyList replies, Configuration config) throws RepositoryException, UnauthorizedException {
        if (!verifyQuorum(replies, config)){
            throw new ServerErrorException();
        }

        return processLedger(replies.getMessage(), (type, ledger) -> {
            switch (type) {
                case OK:
                    return LedgerReplyMessage.deserialize(ledger).getLedger();
                case NOT_FOUND:
                    throw new UserNotFoundException();
                case FORBIDDEN:
                    throw new UnauthorizedException();
                default:
                    throw new ServerErrorException();
            }

        });
    }

    public Set<Transaction> processLedgerGlobal(QuorumReplyList replies, Configuration config) throws RepositoryException, UnauthorizedException {
        if (!verifyQuorum(replies, config)){
            throw new ServerErrorException();
        }

        return processLedger(replies.getMessage(), (type, ledger) -> {
            switch (type) {
                case OK:
                    return LedgerReplyMessage.deserialize(ledger).getLedger();
                case FORBIDDEN:
                    throw new UnauthorizedException();
                default:
                    throw new ServerErrorException();
            }
        });
    }

    private Set<Transaction> processLedger(byte[] replyBytes, Function function) throws RepositoryException, UnauthorizedException {
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

    private boolean verifyQuorum(QuorumReplyList replies, Configuration config) {
        if (replies.getSignatures().size()!= config.getReplyQuorum()) {
            return false;
        }

        for (HashSignaturePair signature : replies.getSignatures()) {
            if (!verifyReply(signature)) {
                return false;
            }
        }

        return true;
    }

    @FunctionalInterface
    interface Function {
        Set<Transaction> apply(ReplyType type, byte[] ledger) throws RepositoryException, IOException, UnauthorizedException;
    }

}
