package app;

import app.commandresults.CommandResult;
import app.commandresults.ExitCommandResult;
import app.commandresults.HelpResultCommand;
import app.commandresults.LogoutCommandResult;
import app.commands.Command;
import app.commands.CommandFactory;
import app.commands.CommandValidator;
import app.exceptions.InvalidAmountException;
import app.exceptions.InvalidCommandException;
import app.models.UserModel;
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

    private CommandResult validateAndExecuteCommand(Command command) throws InvalidCommandException, InvalidAmountException {
        CommandValidator.validateCommand(command, currentUser);
        CommandResult result = command.execute(defaultUri, restTemplate, currentUser);
        result.show();

        if (result instanceof LogoutCommandResult) {
            showHelp();
        }

        return result;
    }

}
