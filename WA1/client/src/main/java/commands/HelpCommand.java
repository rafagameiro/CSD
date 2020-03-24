package commands;

import commandresults.CommandResult;
import commandresults.HelpResultCommand;
import models.UserModel;
import org.springframework.web.client.RestTemplate;

public class HelpCommand implements Command {
    public static final String HELP = "help";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        return new HelpResultCommand();
    }
}
