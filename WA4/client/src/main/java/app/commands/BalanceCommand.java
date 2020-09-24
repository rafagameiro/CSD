package app.commands;

import app.commandresults.BalanceCommandResult;
import app.commandresults.CommandResult;
import app.commandresults.ReplyManager;
import app.models.Amount;
import app.models.AppUser;
import app.models.QuorumReplyList;
import app.models.User;
import app.models.requests.BalanceRequest;
import app.utils.HttpUtils;
import app.utils.RSAKeyLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

public class BalanceCommand implements Command {
    public static final String COMMAND =  "balance";
    public static final String USAGE =  "balance";
    public static final String[] OPT_DESCRIPTION = {};
    public static final String DESCRIPTION = "Shows the balance in the account.";
    public static final String HELP = " balance\t\t\t\t"+DESCRIPTION;
    private static final String PATH = "/wallets/%s";

    RSAKeyLoader loader;

    public BalanceCommand() {
        this.loader = RSAKeyLoader.getInstance();
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        try {
            User currentUser = appUser.getUser();
            String endpoint = String.format(defaultUri + PATH, currentUser.getUsername());
            String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);

            BalanceRequest request = HttpUtils.createRequest(appUser.getContractFilePath(), appUser.getKeyId(), loader, BalanceRequest::new);
            ResponseEntity<QuorumReplyList> response = HttpUtils.requestWithBody(restTemplate, HttpMethod.POST, endpoint, request, authHeader, QuorumReplyList.class);
            return HttpUtils.processResponse(HttpStatus.OK, response, body -> {
                Amount amount = replyManager.processBalance(body, appUser.getConfig());
                return new BalanceCommandResult(amount);
            });

        } catch (Exception e) {
            return new BalanceCommandResult(e.getMessage());
        }
    }
}
