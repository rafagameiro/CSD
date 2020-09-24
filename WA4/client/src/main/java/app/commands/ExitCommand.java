package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.ExitCommandResult;
import app.commandresults.ReplyManager;
import app.models.AppUser;
import app.models.User;
import org.springframework.web.client.RestTemplate;

public class ExitCommand implements Command {
    public static final String COMMAND =  "exit";
    public static final String USAGE =  "exit";
    public static final String DESCRIPTION = "Leaves the system.";
    public static final String HELP = " exit\t\t\t\t\t"+DESCRIPTION;

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        return new ExitCommandResult();
    }
}
