package app.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RedisHash("wallet")
public class Wallet {
    @Id
    private String username;
    private float balance;
    private Set<Transaction> ledger;

    public Wallet() {

    }

    public Wallet(String username) {
        this(username, 0, new HashSet<>());
    }

    public Wallet(String username, float balance, Set<Transaction> ledger) {
        this.username = username;
        this.balance = balance;
        this.ledger = ledger;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void deposit(float amount) {
        this.balance += amount;
    }

    public void addToLedger(Transaction transaction) {
        if (this.ledger == null) {
            this.ledger = new HashSet<>();
        }
        this.ledger.add(transaction);
    }

    public Set<Transaction> getLedger() {
        if (this.ledger == null) {
            this.ledger = new HashSet<>();
        }
        return this.ledger;
    }

    public void setLedger(Set<Transaction> ledger) {
        this.ledger = ledger;
    }

    public boolean canWithdraw(float amount) {
        return this.balance >= amount;
    }

    public void withdraw(float amount) {
        this.balance -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return this.hashCode() == wallet.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
