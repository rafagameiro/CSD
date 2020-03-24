package g2.labs.repository;

import com.google.gson.*;
import g2.labs.exceptions.InsufficientBalanceException;
import g2.labs.exceptions.UserNotFoundException;
import g2.labs.models.Amount;
import g2.labs.models.Transaction;
import g2.labs.models.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.nio.file.*;
import java.io.*;
import java.util.*;

@Repository
public class WalletRepository {
    private static final String FILE_LOCATION = ".config/.data/transfers/transfers.json";
    private static final String FILE_NOT_FOUND = "The transfers file does not exist.";
    private static final int INITIAL_CAPACITY = 100;

    private static Gson gson;
    private JsonArray walletsList;
    private Map<String, Wallet> wallets;
    private Map<String, Integer> walletsPos;

    public WalletRepository() {
        this.wallets = new HashMap<>(INITIAL_CAPACITY);
        this.walletsPos = new HashMap<>(INITIAL_CAPACITY);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Amount balance(String username) throws UserNotFoundException {
        Wallet wallet = wallets.get(username);
        if (wallet == null) {
            throw new UserNotFoundException();
        }
        return wallet.getAmount();
    }

    public void createWallet(String username) {

        wallets.put(username, new Wallet());
        walletsPos.put(username, walletsList.size());

        JsonObject wallet = new JsonObject();
        wallet.addProperty("name", username);
        wallet.addProperty("amount", 0.0);
        wallet.add("ledger", new JsonArray());
        walletsList.add(wallet);


        writeJson();
    }

    public void deposit(String username, Amount amount) throws UserNotFoundException {
        Wallet wallet = wallets.get(username);
        if (wallet == null) {
            throw new UserNotFoundException();
        }
        wallet.add(amount);

        JsonObject walletJson = (JsonObject) walletsList.get(walletsPos.get(username));
        walletJson.addProperty("amount", wallet.getAmount().getAmount());
        writeJson();
    }

    public void transfer(String username, Transaction transaction) throws UserNotFoundException, InsufficientBalanceException {
        Wallet from = wallets.get(username);
        Wallet to = wallets.get(transaction.getTo());
        if (from == null || to == null) {
            throw new UserNotFoundException();
        }
        if (from.getAmount().canWithdraw(transaction.getAmount())) {
            from.getAmount().withdraw(transaction.getAmount());
            from.addToLedger(transaction);
            to.add(transaction.getAmount());
            to.addToLedger(transaction);

            JsonObject walletJsonFrom = (JsonObject) walletsList.get(walletsPos.get(username));
            JsonObject walletJsonTo = (JsonObject) walletsList.get(walletsPos.get(transaction.getTo()));
            walletJsonFrom.addProperty("amount", from.getAmount().getAmount());
            walletJsonTo.addProperty("amount", to.getAmount().getAmount());
            JsonObject transfer = new JsonObject();
            transfer.addProperty("from", username);
            transfer.addProperty("to", transaction.getTo());
            transfer.addProperty("amount", transaction.getAmount().getAmount());
            walletJsonFrom.getAsJsonArray("ledger").add(transfer);
            walletJsonTo.getAsJsonArray("ledger").add(transfer);
            writeJson();

        } else {
            throw new InsufficientBalanceException();
        }
    }

    public List<Transaction> getLedger(String username) throws UserNotFoundException {
        Wallet wallet = wallets.get(username);
        if (wallet == null) {
            throw new UserNotFoundException();
        }
        return wallet.getLedger();
    }

    public Set<Transaction> getLedger() {
        Set<Transaction> ledger = new HashSet<>();
        for (Wallet wallet : wallets.values()) {
            ledger.addAll(wallet.getLedger());
        }
        return ledger;
    }

    @PostConstruct
    private void loadWallets() {

        FileReader fr;
        try {

            fr = new FileReader(FILE_LOCATION);
            walletsList = (new Gson()).fromJson(fr, JsonArray.class);

            int i = 0;
            Iterator<JsonElement> it = walletsList.iterator();
            while (it.hasNext()) {
                JsonObject elem = it.next().getAsJsonObject();

                Wallet wallet = new Wallet();
                wallet.add(new Amount(elem.get("amount").getAsFloat()));

                Iterator<JsonElement> itBids = elem.get("ledger").getAsJsonArray().iterator();
                while (itBids.hasNext()) {

                    JsonObject transfers = itBids.next().getAsJsonObject();
                    wallet.addToLedger(new Transaction(transfers.get("from").getAsString(), transfers.get("to").getAsString(), new Amount(transfers.get("amount").getAsFloat())));
                }
                this.wallets.put(elem.get("name").getAsString(), wallet);
                this.walletsPos.put(elem.get("name").getAsString(), i++);
            }

        } catch (Exception e) {
            createDirectories();
        }
    }

    private void createDirectories() {
        
        try {
            
            Path path = Paths.get(FILE_LOCATION);
            Files.createDirectories(path.getParent());
            new File(FILE_LOCATION).createNewFile();
            walletsList = new JsonArray();

        } catch(IOException e) {
            System.err.println(FILE_NOT_FOUND);
        }
    }

    private void writeJson() {

        FileWriter fw;
        try {
            fw = new FileWriter(FILE_LOCATION);
            gson.toJson(walletsList, fw);
            fw.close();
        } catch (IOException e) {
            System.err.println(FILE_NOT_FOUND);
        }
    }
}
