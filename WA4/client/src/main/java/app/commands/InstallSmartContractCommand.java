package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.InstallSmartContractResult;
import app.commandresults.ReplyManager;
import app.exceptions.SmartContractException;
import app.models.AppUser;
import app.models.User;
import app.utils.SmartContractUtils;
import org.springframework.web.client.RestTemplate;

public class InstallSmartContractCommand implements Command {
    public static final String COMMAND =  "installcontract";
    public static final String USAGE =  "installcontract <filePath>";
    public static final String[] OPT_DESCRIPTION = {"name of smart contract file"};
    public static final String DESCRIPTION = "Installs a smart contract locally.";
    public static final String HELP = " installcontract\t\t"+DESCRIPTION;
    private String filePath;

    public InstallSmartContractCommand(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) throws SmartContractException {
        SmartContractUtils.loadContract(filePath);
        appUser.setContractFilePath(filePath);
        return new InstallSmartContractResult(filePath);
    }

}
