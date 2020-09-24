package app.commands;

import app.commandresults.BalanceCommandResult;
import app.commandresults.CommandResult;
import app.models.Amount;
import app.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import app.utils.RequestUtils;

import java.nio.charset.StandardCharsets;

public class BalanceCommand implements Command {
    public static final String HELP = "balance";
    private static final String PATH = "/wallets/%s";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
        String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);
        ResponseEntity<Amount> response = RequestUtils.request(restTemplate, HttpMethod.GET, endpoint, authHeader, Amount.class);

        boolean success = response.getStatusCode().equals(HttpStatus.OK);
        Amount amount = response.getBody();

        return new BalanceCommandResult(success, amount);
    }
}
