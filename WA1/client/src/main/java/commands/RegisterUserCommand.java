package commands;

import commandresults.CommandResult;
import commandresults.RegisterUserCommandResult;
import models.UserModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utils.RequestUtils;

public class RegisterUserCommand implements Command {
    public static final String HELP = "register <username> <password>";
    private static final String PATH = "/users";

    private UserModel user;

    public RegisterUserCommand(String username, String password) {
        this.user = new UserModel(username, password);
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        String endpoint = defaultUri + PATH;

        ResponseEntity<String> response = RequestUtils.requestWithBody(restTemplate, HttpMethod.POST, endpoint, user, null, String.class);

        boolean success = response.getStatusCode().equals(HttpStatus.CREATED);
        if (success) {
            currentUser.clone(user);
        }
        return new RegisterUserCommandResult(success);
    }

}
