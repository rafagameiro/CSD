package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.RemoveSmartContractResult;
import app.commandresults.ReplyManager;
import app.models.AppUser;
import org.springframework.web.client.RestTemplate;

public class RemoveSmartContractCommand implements Command {
    public static final String COMMAND =  "removecontract";
    public static final String USAGE =  "removecontract";
    public static final String[] OPT_DESCRIPTION = {};
    public static final String DESCRIPTION = "Removes the smart contract currently installed.";
    public static final String HELP = " removecontract\t\t\t"+DESCRIPTION;

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        String path = appUser.getContractFilePath();
        appUser.setContractFilePath(null);
        return new RemoveSmartContractResult(path);
    }
}
