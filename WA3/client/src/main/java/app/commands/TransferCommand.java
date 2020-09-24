package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.TransferCommandResult;
import app.exceptions.InvalidAmountException;
import app.exceptions.InvalidTransactionException;
import app.models.Transaction;
import app.models.UserModel;
import app.utils.RequestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class TransferCommand implements Command {
    public static final String HELP = "transfer <amount> <to>";
    public static final String PATH = "/wallets/%s/ledger";

    private float amount;
    private String to;

    public TransferCommand(String amount, String to) {
        this.amount = Float.parseFloat(amount);
        this.to = to;
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) throws InvalidTransactionException, InvalidAmountException {
        if (currentUser.getUsername().equals(this.to)) {
            throw new InvalidTransactionException();
        }
        if (amount <= 0) {
            throw new InvalidAmountException();
        }

        String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
        String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);
        Transaction transaction = new Transaction(currentUser.getUsername(), this.to, this.amount);
        ResponseEntity<String> response = RequestUtils.requestWithBody(restTemplate, HttpMethod.PUT, endpoint, transaction, authHeader, String.class);

        return new TransferCommandResult(response.getStatusCode());
    }
}
