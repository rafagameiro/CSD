package app.repository;

import app.exceptions.UserAlreadyExistsException;
import app.exceptions.UserNotFoundException;
import app.models.Transaction;
import app.models.Wallet;

import java.util.Set;

public interface WalletRepository {

    Wallet findByUsername(String username) throws UserNotFoundException;

    void save(Wallet wallet) throws UserAlreadyExistsException;

    void update(Wallet wallet) throws UserNotFoundException;

    Set<Transaction> getAllTransactions();
}
