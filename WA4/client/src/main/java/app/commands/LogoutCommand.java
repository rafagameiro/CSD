package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.LogoutCommandResult;
import app.commandresults.ReplyManager;
import app.models.AppUser;
import app.models.User;
import org.springframework.web.client.RestTemplate;

public class LogoutCommand implements Command {
    public static final String COMMAND =  "logout";
    public static final String USAGE =  "logout";
    public static final String DESCRIPTION = "Logs out from the system.";
    public static final String HELP = " logout\t\t\t\t\t"+DESCRIPTION;

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        appUser.getUser().reset();
        appUser.setKeyId(-1);
        appUser.setContractFilePath(null);
        return new LogoutCommandResult();
    }
}
