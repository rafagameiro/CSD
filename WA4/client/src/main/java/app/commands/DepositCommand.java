package app.commands;

import app.CommandLineApp;
import app.commandresults.CommandResult;
import app.commandresults.DepositCommandResult;
import app.commandresults.ReplyManager;
import app.exceptions.InvalidAmountException;
import app.models.AppUser;
import app.models.QuorumReplyList;
import app.models.User;
import app.models.requests.DepositRequest;
import app.utils.HttpUtils;
import app.utils.RSAKeyLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class DepositCommand implements Command {
    public static final String COMMAND =  "deposit";
    public static final String USAGE =  "deposit <amount>";
    public static final String[] OPT_DESCRIPTION = {"amount to add to account"};
    public static final String DESCRIPTION = "Deposit an amount into the account.";
    public static final String HELP = " deposit\t\t\t\t"+DESCRIPTION;
    private static final String PATH = "/wallets/%s";

    private float amount;
    private RSAKeyLoader loader;

    public DepositCommand(String amount) {
        this.amount = Float.parseFloat(amount);
        this.loader = RSAKeyLoader.getInstance();
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        try {
            if (amount <= 0) {
                throw new InvalidAmountException();
            }
            User currentUser = appUser.getUser();

            String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
            String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);

            DepositRequest request = HttpUtils.createRequest(appUser.getContractFilePath(), appUser.getKeyId(), loader, (contract, signature) ->
                    new DepositRequest(amount, contract, signature));
            
            ResponseEntity<QuorumReplyList> response = HttpUtils.requestWithBody(restTemplate, HttpMethod.PUT, endpoint, request, authHeader, QuorumReplyList.class);
            return HttpUtils.processResponse(HttpStatus.OK, response, body -> {
                replyManager.processDeposit(body, appUser.getConfig());
                return new DepositCommandResult();
            });

        } catch (Exception e) {
            return new DepositCommandResult(e.getMessage());
        }
    }
}
