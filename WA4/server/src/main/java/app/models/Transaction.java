package app.models;

import org.springframework.data.redis.core.index.Indexed;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

public class Transaction implements Comparable {
    @Indexed
    private UUID id;
    private String from, to;
    private float amount;

    public Transaction() {

    }

    public Transaction(UUID id, String from, String to, float amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public static Transaction deserialize(byte[] transaction) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(transaction)) {
            try (DataInputStream dis = new DataInputStream(bis)) {

                UUID id = UUID.fromString(dis.readUTF());
                String from = dis.readUTF();
                String to = dis.readUTF();
                float amount = dis.readFloat();
                return new Transaction(id, from, to, amount);
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public float getAmount() {
        return amount;
    }

    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                dos.writeUTF(id.toString());
                dos.writeUTF(this.from);
                dos.writeUTF(this.to);
                dos.writeFloat(this.amount);

                return bos.toByteArray();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        int h1 = this.hashCode();
        int h2 = o.hashCode();

        if (h1 == h2) {
            return 0;
        } else if (h1 > h2) {
            return 1;
        }
        return -1;
    }
}
