package app.commands;

import app.commandresults.CommandResult;
import app.commandresults.ReplyManager;
import app.commandresults.TransferCommandResult;
import app.exceptions.InvalidAmountException;
import app.exceptions.InvalidTransactionException;
import app.models.AppUser;
import app.models.QuorumReplyList;
import app.models.Transaction;
import app.models.User;
import app.models.requests.TransferRequest;
import app.utils.HttpUtils;
import app.utils.RSAKeyLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class TransferCommand implements Command {
    public static final String COMMAND =  "transfer";
    public static final String USAGE =  "transfer <amount> <to>";
    public static final String[] OPT_DESCRIPTION = {"amount to send", "user wo will receive amount"};
    public static final String DESCRIPTION = "Transfers an amount to another user.";
    public static final String HELP = " transfer\t\t\t\t"+DESCRIPTION;
    public static final String PATH = "/wallets/%s/ledger";

    private float amount;
    private String to;
    private RSAKeyLoader loader;

    public TransferCommand(String amount, String to) {
        this.amount = Float.parseFloat(amount);
        this.to = to;
        loader = RSAKeyLoader.getInstance();
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        try {
            User currentUser = appUser.getUser();
            if (currentUser.getUsername().equals(this.to)) {
                throw new InvalidTransactionException();
            }
            if (amount <= 0) {
                throw new InvalidAmountException();
            }

            String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
            String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);

            Transaction transaction = new Transaction(currentUser.getUsername(), this.to, this.amount);
            TransferRequest request = HttpUtils.createRequest(appUser.getContractFilePath(),appUser.getKeyId(),loader,(contract, signature) -> new TransferRequest(transaction,contract,signature));

            ResponseEntity<QuorumReplyList> response = HttpUtils.requestWithBody(restTemplate, HttpMethod.PUT, endpoint, request, authHeader, QuorumReplyList.class);
            return HttpUtils.processResponse(HttpStatus.OK, response, body -> {
                replyManager.processTransfer(body, appUser.getConfig());
                return new TransferCommandResult();
            });
        } catch (Exception e) {
            return new TransferCommandResult(e);
        }
    }
}
