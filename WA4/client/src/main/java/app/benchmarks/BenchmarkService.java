package app.benchmarks;

import app.commandresults.CommandResult;
import app.commandresults.ReplyManager;
import app.commands.*;
import app.exceptions.SmartContractException;
import app.models.AppUser;
import app.models.User;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BenchmarkService {

    private static final int NUM_THREADS = 2;
    private static int OPERATIONS = 10;

    private static final String REGISTER_USERS = "REGISTERUSERS";
    private static final String DEPOSIT = "DEPOSIT";
    private static final String TRANSFER = "TRANSFER";
    private static final String LEDGER = "LEDGER";
    private static String[] baseUsers = {"hj", "leitao", "damasio", "alferes", "duarte", "leite", "mamede", "goulao"};
    private static String[] basePasswords = {"hjhjhj", "assado", "carlos", "oi", "vitorsergio", "joao", "ada", "md"};

    private ReplyManager rManager;
    private final Map<String, Long> benchmarks;
    private List<User> registeredUsers;
    private RestTemplate restTemplate;
    private String defaultUri;

    public BenchmarkService(RestTemplate restTemplate, String defaultUri) {
        this.rManager = new ReplyManager();
        this.benchmarks = new HashMap<>(4);
        this.registeredUsers = new LinkedList<>();
        this.restTemplate = restTemplate;
        this.defaultUri = defaultUri;
    }

    private void testRegisterUsers(Map<String, Long> benchmarks) {
        log("Initializing test register users!");

        long sum = 0;
        for (int i = 0; i < OPERATIONS; i++) {
            int idx = i % baseUsers.length;
            String username = baseUsers[idx] + Thread.currentThread().getId() + "" + i;
            String password = basePasswords[idx];
            AppUser app = new AppUser(new User(username, password), null, -1);
            RegisterUserCommand command = new RegisterUserCommand(username, password, "1000");

            Long start = System.currentTimeMillis();
            CommandResult result = command.execute(defaultUri, restTemplate, app, this.rManager);
            Long end = System.currentTimeMillis();
            sum += (end - start);

            result.show();
            registeredUsers.add(app.getUser());

            log("Test register users operation " + i);
        }

        benchmarks.put(REGISTER_USERS, sum / OPERATIONS);
        log("Test register users completed!");
    }

    private void testDeposit(Map<String, Long> benchmarks) {
        log("Initializing test deposit!");

        long sum = 0;
        for (int i = 0; i < OPERATIONS; i++) {
            User user = registeredUsers.get(i % registeredUsers.size());
            AppUser app = new AppUser(user, null, -1);
            new LoginCommand(user.getUsername(), user.getPassword(), "1000")
                    .execute(defaultUri, restTemplate, app, this.rManager);
            try {
                new InstallSmartContractCommand("testcontract.js").execute(defaultUri, restTemplate, app, this.rManager);
            } catch (SmartContractException e) {
                // ignored
                System.err.println(e.getMessage());
            }
            DepositCommand command = new DepositCommand(String.valueOf(i + 1));

            Long start = System.currentTimeMillis();
            CommandResult result = command.execute(defaultUri, restTemplate, app, this.rManager);
            Long end = System.currentTimeMillis();
            sum += (end - start);

            result.show();
            log("Test deposit operation " + i);
        }

        benchmarks.put(DEPOSIT, sum / OPERATIONS);
        log("Test deposit completed!");
    }

    private void testTransfer(Map<String, Long> benchmarks) {
        System.out.println("Initializing test transfer!");

        long sum = 0;
        for (int i = 0; i < OPERATIONS; i++) {
            User from = registeredUsers.get(i % registeredUsers.size());
            User to = registeredUsers.get((i + 1) % registeredUsers.size());
            AppUser app = new AppUser(from, null, -1);
            new LoginCommand(from.getUsername(), from.getPassword(), "1000")
                    .execute(defaultUri, restTemplate, app, this.rManager);

            try {
                new InstallSmartContractCommand("testcontract.js").execute(defaultUri, restTemplate, app, rManager);
            } catch (SmartContractException e) {
                // ignored
                System.err.println(e.getMessage());
            }

            TransferCommand command = new TransferCommand(String.valueOf(i + 1), to.getUsername());

            Long start = System.currentTimeMillis();
            CommandResult result = command.execute(defaultUri, restTemplate, app, rManager);
            Long end = System.currentTimeMillis();
            sum += (end - start);

            result.show();
            log("Test transfer operation " + i);
        }

        benchmarks.put(TRANSFER, sum / OPERATIONS);
        log("Test transfer completed!");
    }

    private void testLedger(Map<String, Long> benchmarks) {
        log("Initializing test ledger!");

        long sum = 0;
        for (int i = 0; i < OPERATIONS; i++) {
            User user = registeredUsers.get(i % registeredUsers.size());
            AppUser app = new AppUser(user, null, -1);
            new LoginCommand(user.getUsername(), user.getPassword(), "1000")
                    .execute(defaultUri, restTemplate, app, rManager);

            try {
                new InstallSmartContractCommand("testcontract.js").execute(defaultUri, restTemplate, app, rManager);
            } catch (SmartContractException e) {
                // ignored
                System.err.println(e.getMessage());
            }

            LedgerGlobalCommand command = new LedgerGlobalCommand();

            Long start = System.currentTimeMillis();
            CommandResult result = command.execute(defaultUri, restTemplate, app, rManager);
            Long end = System.currentTimeMillis();
            sum += (end - start);

            result.show();
            log("Test ledger operation " + i);
        }

        benchmarks.put(LEDGER, sum / OPERATIONS);
        log("Test ledger completed!");
    }

    private void printBenchmarks() {
        for (Map.Entry<String, Long> entry : benchmarks.entrySet()) {
            System.out.println(entry.getKey() + ":");
            System.out.println(String.format("Operations executed: %s\t Time ellapsed(avg): %d ms\t", OPERATIONS, entry.getValue()));
        }
    }

    private void workbench() {
        Map<String, Long> benchmarks = new HashMap<>();
        testRegisterUsers(benchmarks);
        testDeposit(benchmarks);
        testTransfer(benchmarks);
        testLedger(benchmarks);
        synchronized (this.benchmarks) {
            long currRU = benchmarks.get(REGISTER_USERS);

            if (this.benchmarks.containsKey(REGISTER_USERS)) {
                long prev = this.benchmarks.get(REGISTER_USERS);
                long res = (prev+currRU)/2;
                this.benchmarks.put(REGISTER_USERS, res);
            } else {
                this.benchmarks.put(REGISTER_USERS, currRU);
            }

            long currD = benchmarks.get(DEPOSIT);

            if (this.benchmarks.containsKey(DEPOSIT)) {
                long prev = this.benchmarks.get(DEPOSIT);
                long res = (prev+currD)/2;
                this.benchmarks.put(DEPOSIT, res);
            } else {
                this.benchmarks.put(DEPOSIT, currD);
            }

            long currT = benchmarks.get(TRANSFER);

            if (this.benchmarks.containsKey(TRANSFER)) {
                long prev = this.benchmarks.get(TRANSFER);
                long res = (prev+currT)/2;
                this.benchmarks.put(TRANSFER, res);
            } else {
                this.benchmarks.put(TRANSFER, currT);
            }

            long currL = benchmarks.get(LEDGER);

            if (this.benchmarks.containsKey(LEDGER)) {
                long prev = this.benchmarks.get(LEDGER);
                long res = (prev+currL)/2;
                this.benchmarks.put(LEDGER, res);
            } else {
                this.benchmarks.put(LEDGER, currL);
            }
        }
    }

    private static class Task implements Callable<Object> {
        private Runnable r;

        Task(Runnable r) {
            this.r = r;
        }

        @Override
        public Object call() {
            r.run();
            return null;
        }
    }

    public void execute() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < NUM_THREADS; i++) {
                tasks.add(new Task(this::workbench));
            }
            executor.invokeAll(tasks);
            executor.shutdown();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        printBenchmarks();
    }

    private void log(String msg) {
        System.out.println("[" + Thread.currentThread().getId() + "] - " + msg);
    }
}
