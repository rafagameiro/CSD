package app.repository;

import app.exceptions.UserAlreadyExistsException;
import app.exceptions.UserNotFoundException;
import app.models.Transaction;
import app.models.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class WalletRepositoryImpl implements WalletRepository {
    private WalletRepositoryWrapper repository;

    @Autowired
    public WalletRepositoryImpl(WalletRepositoryWrapper repository) {
        this.repository = repository;
    }

    public Wallet findByUsername(String username) throws UserNotFoundException {
        Optional<Wallet> user = repository.findById(username);
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    @Override
    public void save(Wallet wallet) throws UserAlreadyExistsException {
        Optional<Wallet> old = repository.findById(wallet.getUsername());
        if (old.isPresent()) {
            throw new UserAlreadyExistsException();

        }

        repository.save(wallet);
    }

    @Override
    public void update(Wallet wallet) throws UserNotFoundException {
        Optional<Wallet> old = repository.findById(wallet.getUsername());
        if (!old.isPresent()) {
            throw new UserNotFoundException();
        }

        repository.save(wallet);
    }

    @Override
    public Set<Transaction> getAllTransactions() {
        Iterable<Wallet> wallets = repository.findAll();
        Set<Transaction> transactions = new HashSet<>();
        wallets.forEach(it -> transactions.addAll(it.getLedger()));

        Set<Transaction> transactionsSorted = transactions.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));

        return transactionsSorted;
    }

}
