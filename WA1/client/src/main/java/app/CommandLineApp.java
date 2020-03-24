package app;

import commandresults.CommandResult;
import commandresults.ExitCommandResult;
import commandresults.HelpResultCommand;
import commandresults.LogoutCommandResult;
import commands.Command;
import commands.CommandFactory;
import commands.CommandValidator;
import exceptions.InvalidCommandException;
import models.UserModel;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class CommandLineApp {
    private RestTemplate restTemplate;
    private String defaultUri;
    private UserModel currentUser;

    public CommandLineApp(RestTemplate restTemplate, String defaultUri) {
        this.restTemplate = restTemplate;
        this.defaultUri = defaultUri;
        this.currentUser = new UserModel();
    }

    public void run() {
        showHelp();
        Scanner in = new Scanner(System.in);
        CommandResult result = null;
        do {
            try {
                result = validateAndExecuteCommand(parseCommand(in));
            } catch (Exception e) {
                showError(e);
            }
        } while (!toExit(result));
    }

    private Command parseCommand(Scanner in) throws InvalidCommandException {
        String line = in.nextLine();
        return CommandFactory.from(line);
    }

    private boolean toExit(CommandResult result) {
        return result instanceof ExitCommandResult;
    }

    private void showHelp() {
        CommandResult help = new HelpResultCommand();
        help.show();
    }

    private void showError(Exception exception) {
        System.out.println(exception.getMessage());
    }

    private CommandResult validateAndExecuteCommand(Command command) throws InvalidCommandException {
        CommandValidator.validateCommand(command, currentUser);
        CommandResult result = command.execute(defaultUri, restTemplate, currentUser);
        result.show();

        if (result instanceof LogoutCommandResult) {
            showHelp();
        }

        return result;
    }

}
