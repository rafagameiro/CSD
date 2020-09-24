package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.ManualCommandResult;
import app.commandresults.ReplyManager;
import app.models.AppUser;
import org.springframework.web.client.RestTemplate;

public class ManualCommand implements Command {
    public static final String COMMAND = "man";
    public static final String USAGE = "man";
    public static final String DESCRIPTION = "Shows information on how to use a command.";
    public static final String HELP = " man\t\t\t\t\t" + DESCRIPTION;
    private String command;

    public ManualCommand(String command) {
        this.command = command;
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        if (appUser.getUser().isEmpty())
            return new ManualCommandResult(command, true);
        else
            return new ManualCommandResult(command, false);


    }
}