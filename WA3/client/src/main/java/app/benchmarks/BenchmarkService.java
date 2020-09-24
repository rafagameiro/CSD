package app.benchmarks;

import app.commandresults.CommandResult;
import app.commands.*;
import app.models.UserModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BenchmarkService {

    private static final String REGISTERUSERS = "REGISTERUSERS";
    private static final String DEPOSIT = "DEPOSIT";
    private static final String TRANSFER = "TRANSFER";
    private static final String LEDGER = "LEDGER";
    private static String[] baseUsers = {"hj", "leitao", "damasio", "alferes", "duarte", "leite", "mamede", "goulao"};
    private static String[] basePasswords = {"hjhjhj", "assado", "carlos", "oi", "vitorsergio", "joao", "ada", "md"};
    private static int operations = 3;

    private Map<String, Long> benchmarks;
    private List<UserModel> registeredUsers;
    private RestTemplate restTemplate;
    private String defaultUri;
    private UserModel currentUser;

    public BenchmarkService(RestTemplate restTemplate, String defaultUri) {
        this.benchmarks = new HashMap<>(4);
        this.registeredUsers = new LinkedList<>();
        this.restTemplate = restTemplate;
        this.defaultUri = defaultUri;
        this.currentUser = new UserModel();
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
                RegisterUserCommand command = new RegisterUserCommand(username, password);
                CommandValidator.validateCommand(command, currentUser);
                CommandResult result = command.execute(defaultUri, restTemplate, currentUser);
                result.show();
                new LogoutCommand().execute(defaultUri, restTemplate, currentUser);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            registeredUsers.add(new UserModel(username, password));

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
            UserModel user = registeredUsers.get(i % registeredUsers.size());
            new LogoutCommand().execute(defaultUri, restTemplate, currentUser);
            new LoginCommand(user.getUsername(), user.getPassword()).execute(defaultUri, restTemplate, currentUser);
            Long start = System.currentTimeMillis();
            try {
                DepositCommand command = new DepositCommand(String.valueOf(i + 1));
                CommandValidator.validateCommand(command, user);
                CommandResult result = command.execute(defaultUri, restTemplate, currentUser);
                result.show();
                new LogoutCommand().execute(defaultUri, restTemplate, currentUser);
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
            UserModel from = registeredUsers.get(i % registeredUsers.size());
            UserModel to = registeredUsers.get((i + 1) % registeredUsers.size());
            new LogoutCommand().execute(defaultUri, restTemplate, currentUser);
            new LoginCommand(from.getUsername(), from.getPassword()).execute(defaultUri, restTemplate, currentUser);

            Long start = System.currentTimeMillis();

            try {
                TransferCommand command = new TransferCommand(String.valueOf(i + 1), to.getUsername());
                CommandValidator.validateCommand(command, from);
                CommandResult result = command.execute(defaultUri, restTemplate, currentUser);
                result.show();
                new LogoutCommand().execute(defaultUri, restTemplate, currentUser);
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
            UserModel user = registeredUsers.get(i % registeredUsers.size());
            new LogoutCommand().execute(defaultUri, restTemplate, currentUser);
            new LoginCommand(user.getUsername(), user.getPassword()).execute(defaultUri, restTemplate, currentUser);
            if (i % 2 == 0) {
                Long start = System.currentTimeMillis();
                try {
                    LedgerClientCommand command = new LedgerClientCommand(user.getUsername());
                    CommandValidator.validateCommand(command, user);
                    CommandResult result = command.execute(defaultUri, restTemplate, currentUser);
                    result.show();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                Long end = System.currentTimeMillis();
                sum += (end - start);
            }

            Long start = System.currentTimeMillis();
            try {
                LedgerGlobalCommand command = new LedgerGlobalCommand();
                CommandValidator.validateCommand(command, user);
                CommandResult result = command.execute(defaultUri, restTemplate, currentUser);
                result.show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            new LogoutCommand().execute(defaultUri, restTemplate, currentUser);

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
