package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.LoginCommandResult;
import app.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import app.utils.RequestUtils;

import java.nio.charset.StandardCharsets;

public class LoginCommand implements Command {
    public static final String HELP = "login <username> <password>";
    private static final String PATH = "/login";

    private UserModel user;

    public LoginCommand(String username, String password) {
        this.user = new UserModel(username, password);
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        String endpoint = defaultUri + PATH;
        String authHeader = HttpHeaders.encodeBasicAuth(user.getUsername(), user.getPassword(), StandardCharsets.UTF_8);

        ResponseEntity<String> response = RequestUtils.requestWithBody(restTemplate, HttpMethod.POST, endpoint, user, authHeader, String.class);

        boolean success = response.getStatusCode().equals(HttpStatus.OK);
        if (success) {
            currentUser.clone(user);
        }
        return new LoginCommandResult(success);
    }
}
