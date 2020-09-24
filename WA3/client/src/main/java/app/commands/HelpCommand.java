package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.HelpResultCommand;
import app.models.UserModel;
import org.springframework.web.client.RestTemplate;

public class HelpCommand implements Command {
    public static final String HELP = "help";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        return new HelpResultCommand();
    }
}
