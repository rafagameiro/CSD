package commands;

import commandresults.CommandResult;
import commandresults.LogoutCommandResult;
import models.UserModel;
import org.springframework.web.client.RestTemplate;

public class LogoutCommand implements Command {
    public static final String HELP = "logout";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        currentUser.reset();
        return new LogoutCommandResult();
    }
}
