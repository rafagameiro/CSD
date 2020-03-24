package commands;

import commandresults.CommandResult;
import commandresults.DepositCommandResult;
import models.Amount;
import models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utils.RequestUtils;

import java.nio.charset.StandardCharsets;

public class DepositCommand implements Command {
    public static final String HELP = "deposit <amount>";
    private static final String PATH = "/wallets/%s";

    private Amount amount;

    public DepositCommand(String amount) {
        this.amount = new Amount(Float.valueOf(amount));
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
        String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);

        ResponseEntity<String> response = RequestUtils.requestWithBody(restTemplate, HttpMethod.PUT, endpoint, amount, authHeader, String.class);
        boolean success = response.getStatusCode().equals(HttpStatus.OK);

        return new DepositCommandResult(success);
    }
}
