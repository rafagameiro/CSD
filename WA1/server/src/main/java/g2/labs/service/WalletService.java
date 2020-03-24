package g2.labs.service;

import g2.labs.exceptions.InsufficientBalanceException;
import g2.labs.exceptions.UserAlreadyExistsException;
import g2.labs.exceptions.UserNotFoundException;
import g2.labs.models.Amount;
import g2.labs.models.Transaction;
import g2.labs.models.UserModel;
import g2.labs.repository.UserRepository;
import g2.labs.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WalletService {
    private UserRepository userRepository;
    private WalletRepository walletRepository;

    @Autowired
    public WalletService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public void createUser(UserModel user) throws UserAlreadyExistsException {
        userRepository.createUser(user.getUsername(),user.getPassword());
        walletRepository.createWallet(user.getUsername());
    }

    public boolean verifyUser(String username, String password) {
        return userRepository.userExists(username,password);
    }

    public Amount balance(String username) throws UserNotFoundException {
        return walletRepository.balance(username);
    }

    public void deposit(String username, Amount amount) throws UserNotFoundException {
        walletRepository.deposit(username, amount);
    }

    public void transfer(String username, Transaction transaction) throws UserNotFoundException, InsufficientBalanceException {
        walletRepository.transfer(username, transaction);
    }

    public List<Transaction> getLedger(String username) throws UserNotFoundException {
        return walletRepository.getLedger(username);
    }

    public Set<Transaction> getLedger() {
        return walletRepository.getLedger();
    }
}
