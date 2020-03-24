package commands;

import commandresults.CommandResult;
import commandresults.TransferCommandResult;
import exceptions.InvalidTransactionException;
import models.Amount;
import models.Transaction;
import models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utils.RequestUtils;

import java.nio.charset.StandardCharsets;

public class TransferCommand implements Command {
    public static final String HELP = "transfer <amount> <to>";
    public static final String PATH = "/wallets/%s/ledger";

    private Amount amount;
    private String to;

    public TransferCommand(String amount, String to) {
        this.amount = new Amount(Float.valueOf(amount));
        this.to = to;
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) throws InvalidTransactionException {
        if (currentUser.getUsername().equals(this.to)) {
            throw new InvalidTransactionException();
        }
        String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
        String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);
        Transaction transaction = new Transaction(currentUser.getUsername(), this.to, this.amount);
        ResponseEntity<String> response = RequestUtils.requestWithBody(restTemplate, HttpMethod.PUT, endpoint, transaction, authHeader, String.class);

        return new TransferCommandResult(response.getStatusCode());
    }
}
