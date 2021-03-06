package commands;

import commandresults.CommandResult;
import commandresults.TestCommandResult;
import models.UserModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utils.RequestUtils;

public class TestCommand implements Command {
    public static final String HELP = "test";
    private static final String PATH = "/test";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        String endpoint = defaultUri + PATH;
        ResponseEntity<String> response = RequestUtils.request(restTemplate, HttpMethod.GET, endpoint, null, String.class);
        return new TestCommandResult(response.getBody());
    }
}
