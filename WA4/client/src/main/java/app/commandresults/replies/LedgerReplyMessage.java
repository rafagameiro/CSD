package app.commandresults.replies;

import app.commandresults.IMessage;
import app.models.Transaction;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class LedgerReplyMessage implements IMessage {

    private Set<Transaction> ledger;

    public LedgerReplyMessage(Set<Transaction> ledger) {
        this.ledger = ledger;
    }

    public static LedgerReplyMessage deserialize(byte[] message) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                int size = dis.readInt();
                Set<Transaction> ledger = new HashSet<>();

                int tSize;
                byte[] transaction;
                for (int i = 0; i < size; i++) {
                    tSize = dis.readInt();
                    transaction = new byte[tSize];
                    dis.read(transaction);
                    ledger.add(Transaction.deserialize(transaction));
                }

                return new LedgerReplyMessage(ledger);
            }
        }
    }

    public Set<Transaction> getLedger() {
        return ledger;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeInt(this.ledger.size());
                for (Transaction transaction : ledger) {
                    byte[] transactionBytes = transaction.getBytes();
                    dos.writeInt(transactionBytes.length);
                    dos.write(transactionBytes);
                }

                return bos.toByteArray();
            }
        }
    }
}
