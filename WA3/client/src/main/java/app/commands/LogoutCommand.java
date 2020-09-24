package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.LogoutCommandResult;
import app.models.UserModel;
import org.springframework.web.client.RestTemplate;

public class LogoutCommand implements Command {
    public static final String HELP = "logout";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        currentUser.reset();
        return new LogoutCommandResult();
    }
}
