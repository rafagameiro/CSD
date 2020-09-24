package app.benchmarks;

import app.models.Amount;
import app.models.Transaction;
import app.models.User;
import app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BenchmarkService {

    private static final String REGISTERUSERS = "REGISTERUSERS";
    private static final String DEPOSIT = "DEPOSIT";
    private static final String TRANSFER = "TRANSFER";
    private static final String LEDGER = "LEDGER";
    private static String[] baseUsers = {"hj", "leitao", "damasio", "alferes", "duarte", "leite", "mamede", "goulao"};
    private static String[] basePasswords = {"hjhjhj", "assado", "carlos", "oi", "vitorsergio", "joao", "ada", "md"};
    private static int operations = 300;

    private WalletService walletService;
    private Map<String, Long> benchmarks;
    private List<String> registeredUsers;

    @Autowired
    public BenchmarkService(WalletService walletService) {
        this.walletService = walletService;
        this.benchmarks = new HashMap<>(4);
        this.registeredUsers = new LinkedList<>();
    }

    public void testRegisterUsers() {
        System.out.println("Initializing test register users!");

        long sum = 0;
        for (int i = 0; i < operations; i++) {
            int idx = i % baseUsers.length;
            String username = baseUsers[idx] + i;
            String password = basePasswords[idx];

            Long start = System.currentTimeMillis();
            try {
                walletService.createUser(new User(username, password), null);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            registeredUsers.add(username);

            Long end = System.currentTimeMillis();
            sum += (end - start);
            System.out.println("Test register users operation " + i);
        }

        benchmarks.put(REGISTERUSERS, sum / operations);
        System.out.println("Test register users completed!");
    }

    public void testDeposit() {
        System.out.println("Initializing test deposit!");

        long sum = 0;
        for (int i = 0; i < operations; i++) {
            String username = registeredUsers.get(i % registeredUsers.size());

            Long start = System.currentTimeMillis();
            try {
              //  walletService.deposit(username, i + 1, null);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Long end = System.currentTimeMillis();
            sum += (end - start);
            System.out.println("Test deposit operaiton " + i);
        }

        benchmarks.put(DEPOSIT, sum / operations);
        System.out.println("Test deposit completed!");
    }

    public void testTransfer() {
        System.out.println("Initializing test transfer!");

        long sum = 0;
        for (int i = 0; i < operations; i++) {
            String from = registeredUsers.get(i % registeredUsers.size());
            String to = registeredUsers.get((i + 1) % registeredUsers.size());

            Long start = System.currentTimeMillis();
            try {
               // walletService.transfer(new Transaction(UUID.randomUUID(), from, to, i + 1), null);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            Long end = System.currentTimeMillis();
            sum += (end - start);
            System.out.println("Test transfer operation " + i);
        }
        benchmarks.put(TRANSFER, sum / operations);
        System.out.println("Test transfer completed!");
    }

    public void testLedger() {
        System.out.println("Initializing test ledger!");

        long sum = 0;
        for (int i = 0; i < operations; i++) {
            if (i % 2 == 0) {
                Long start = System.currentTimeMillis();
                try {
                    //walletService.getLedger(registeredUsers.get(i % registeredUsers.size()), null);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                Long end = System.currentTimeMillis();
                sum += (end - start);
            }

            Long start = System.currentTimeMillis();
            try {
                walletService.getLedger(null, null);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Long end = System.currentTimeMillis();
            sum += (end - start);
            System.out.println("Test ledger operation " + i);
        }

        benchmarks.put(LEDGER, sum / operations);
        System.out.println("Test ledger completed!");
    }

    private void printBenchmarks() {
        for (Map.Entry<String, Long> entry : benchmarks.entrySet()) {
            System.out.println(entry.getKey() + ":");
            System.out.println(String.format("Operations executed: %s\t Time ellapsed(avg): %d ms\t", operations, entry.getValue()));
        }
    }

    public void execute() {
        testRegisterUsers();
        testDeposit();
        testTransfer();
        testLedger();
        printBenchmarks();
    }
}
