package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.ExitCommandResult;
import app.models.UserModel;
import org.springframework.web.client.RestTemplate;

public class ExitCommand implements Command {
    public static final String HELP = "exit";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        return new ExitCommandResult();
    }
}
