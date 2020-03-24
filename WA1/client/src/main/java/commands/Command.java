package commands;

import commandresults.CommandResult;
import exceptions.InvalidCommandException;
import models.UserModel;
import org.springframework.web.client.RestTemplate;

public interface Command {
    CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) throws InvalidCommandException;
}
