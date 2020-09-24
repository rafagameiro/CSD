package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.ReplyManager;
import app.exceptions.InvalidAmountException;
import app.exceptions.InvalidCommandException;
import app.exceptions.SmartContractException;
import app.models.AppUser;
import app.models.User;
import org.springframework.web.client.RestTemplate;

public interface Command {

    CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) throws InvalidCommandException, SmartContractException;
}
