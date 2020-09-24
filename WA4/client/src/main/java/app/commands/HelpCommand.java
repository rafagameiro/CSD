package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.HelpResultCommand;
import app.commandresults.ReplyManager;
import app.models.AppUser;
import app.models.User;
import org.springframework.web.client.RestTemplate;

public class HelpCommand implements Command {
    public static final String COMMAND = "help";
    public static final String USAGE =  "help";
    public static final String[] OPT_DESCRIPTION = {};
    public static final String DESCRIPTION = "Lists the available commands.";
    public static final String HELP = " help\t\t\t\t\t"+DESCRIPTION;

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        User currentUser = appUser.getUser();
        if(currentUser.isEmpty())
            return new HelpResultCommand(true);
        else
            return new HelpResultCommand();
    }
}
