package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.DepositCommandResult;
import app.exceptions.InvalidAmountException;
import app.exceptions.InvalidTransactionException;
import app.models.Amount;
import app.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import app.utils.RequestUtils;

import java.nio.charset.StandardCharsets;

public class DepositCommand implements Command {
    public static final String HELP = "deposit <amount>";
    private static final String PATH = "/wallets/%s";

    private Amount amount;

    public DepositCommand(String amount) {
        this.amount = new Amount(Float.valueOf(amount));
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) throws InvalidAmountException {
        if (amount.getAmount()<=0) {
            throw new InvalidAmountException();
        }

        String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
        String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);

        ResponseEntity<String> response = RequestUtils.requestWithBody(restTemplate, HttpMethod.PUT, endpoint, amount, authHeader, String.class);
        boolean success = response.getStatusCode().equals(HttpStatus.OK);

        return new DepositCommandResult(success);
    }
}
