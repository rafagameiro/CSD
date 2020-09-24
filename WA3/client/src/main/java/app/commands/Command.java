package app.commands;

import app.commandresults.CommandResult;
import app.exceptions.InvalidAmountException;
import app.exceptions.InvalidCommandException;
import app.models.UserModel;
import org.springframework.web.client.RestTemplate;

public interface Command {
    CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) throws InvalidCommandException, InvalidAmountException;
}
