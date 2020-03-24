package g2.labs;

import g2.labs.exceptions.InsufficientBalanceException;
import g2.labs.exceptions.UserAlreadyExistsException;
import g2.labs.exceptions.UserNotFoundException;
import g2.labs.models.Amount;
import g2.labs.models.Transaction;
import g2.labs.models.UserModel;
import g2.labs.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class WalletController {
    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping(path = {"/test"})
    public String test() {
        return "welcome to the worlds most famous e-wallet!";
    }

    @PostMapping(path = {"/users"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createUser(@RequestBody UserModel body) throws UserAlreadyExistsException {
        walletService.createUser(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = {"/login"})
    public ResponseEntity login() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = {"/wallets/{u}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Amount> balance(@PathVariable("u") String username) throws UserNotFoundException {
        Amount amount = walletService.balance(username);
        return ResponseEntity.ok(amount);
    }

    @PutMapping(path = {"/wallets/{u}"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity deposit(@PathVariable("u") String username, @RequestBody Amount amount) throws UserNotFoundException {
        walletService.deposit(username, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = {"/wallets/{u}/ledger"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity transfer(@PathVariable("u") String username, @RequestBody Transaction transaction) throws UserNotFoundException, InsufficientBalanceException {
        walletService.transfer(username, transaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = {"/wallets/{u}/ledger"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Transaction>> getClientLedger(@PathVariable("u") String username) throws UserNotFoundException {
        List<Transaction> ledger = walletService.getLedger(username);
        return ResponseEntity.ok(ledger);
    }

    @GetMapping(path = {"/wallets/ledger"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<Transaction>> getGlobalLedger() {
        Set<Transaction> ledger = walletService.getLedger();
        return ResponseEntity.ok(ledger);
    }
}
