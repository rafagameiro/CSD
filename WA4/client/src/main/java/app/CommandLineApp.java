package app;

import app.commandresults.*;
import app.commands.Command;
import app.commands.CommandFactory;
import app.commands.CommandValidator;
import app.exceptions.InvalidCommandException;
import app.exceptions.SmartContractException;
import app.models.AppUser;
import app.models.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CommandLineApp {

    private RestTemplate restTemplate;
    private String defaultUri;
    private AppUser currentUser;
    private ReplyManager replyManager;

    public CommandLineApp(RestTemplate restTemplate, String defaultUri, ReplyManager replyManager) {
        this.restTemplate = restTemplate;
        this.defaultUri = defaultUri;
        this.replyManager = replyManager;
        this.currentUser = new AppUser();
    }

    public void run() {
        loadConfig();

        showHelp(true);
        Scanner in = new Scanner(System.in);
        CommandResult result = null;
        do {
            try {
                System.out.print("> ");
                result = validateAndExecuteCommand(parseCommand(in));
            } catch (Exception e) {
                showError(e);
            }
        } while (!toExit(result));
    }

    private Command parseCommand(Scanner in) throws InvalidCommandException {
        String line = in.nextLine();
        return currentUser.getUser().isEmpty() ? CommandFactory.fromStart(line) : CommandFactory.from(line);
    }

    private boolean toExit(CommandResult result) {
        return result instanceof ExitCommandResult;
    }

    private void showHelp(boolean init) {
        CommandResult help = new HelpResultCommand(init);
        help.show();
    }

    private void showError(Exception exception) {
        System.out.println(exception.getMessage());
    }

    private CommandResult validateAndExecuteCommand(Command command) throws InvalidCommandException, SmartContractException {
        CommandValidator.validateCommand(command, currentUser);
        CommandResult result = command.execute(defaultUri, restTemplate, currentUser, replyManager);
        result.show();

        if (result instanceof LoginCommandResult) {
            if (((LoginCommandResult) result).isLogged()) {
                showHelp(false);
            }
        }

        if (result instanceof RegisterUserCommandResult) {
            if (((RegisterUserCommandResult) result).isSuccess()) {
                showHelp(false);
            }
        }

        if (result instanceof LogoutCommandResult) {
            showHelp(true);
        }

        return result;
    }

    private void loadConfig() {
        Configuration config = new Configuration();
        config.init();
        this.currentUser.setConfig(config);
    }
}
