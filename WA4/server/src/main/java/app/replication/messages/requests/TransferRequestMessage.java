package app.replication.messages.requests;

import app.models.Transaction;
import app.replication.messages.IMessage;

import java.io.*;

public class TransferRequestMessage implements IMessage {

    private Transaction transaction;

    public TransferRequestMessage(Transaction transaction) {
        this.transaction = transaction;
    }

    public static TransferRequestMessage deserialize(byte[] message) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                int size = dis.readInt();
                byte[] t = new byte[size];
                dis.read(t);
                Transaction transaction = Transaction.deserialize(t);
                return new TransferRequestMessage(transaction);
            }
        }
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                byte[] transaction = this.transaction.getBytes();
                dos.writeInt(transaction.length);
                dos.write(transaction);

                return bos.toByteArray();
            }
        }
    }
}
