package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.ReplyManager;
import app.commandresults.TestCommandResult;
import app.models.AppUser;
import app.models.User;
import app.utils.HttpUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TestCommand implements Command {
    public static final String COMMAND =  "test";
    public static final String USAGE = "test";
    public static final String DESCRIPTION = "Executes a test command.";
    public static final String HELP = " test\t\t\t\t\t"+DESCRIPTION;
    private static final String PATH = "/test";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        String endpoint = defaultUri + PATH;
        ResponseEntity<String> response = HttpUtils.request(restTemplate, HttpMethod.GET, endpoint, null, String.class);
        return new TestCommandResult(response.getBody());
    }
}
