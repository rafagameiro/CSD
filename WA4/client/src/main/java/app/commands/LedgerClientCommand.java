package app.commands;

import app.CommandLineApp;
import app.commandresults.CommandResult;
import app.commandresults.LedgerClientCommandResult;
import app.commandresults.ReplyManager;
import app.exceptions.ServerErrorException;
import app.exceptions.UnauthorizedException;
import app.models.AppUser;
import app.models.requests.LedgerRequest;
import app.models.QuorumReplyList;
import app.models.Transaction;
import app.models.User;
import app.utils.RSAKeyLoader;
import app.utils.HttpUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class LedgerClientCommand implements Command {
    public static final String COMMAND =  "ledgerclient";
    public static final String USAGE =  "ledgerclient <who>";
    public static final String[] OPT_DESCRIPTION = {"user to list transactions"};
    public static final String DESCRIPTION = "Lists all transactions of an user.";
    public static final String HELP = " ledgerclient\t\t\t"+DESCRIPTION;
    private static final String PATH = "/wallets/%s/ledger";

    private String username;
    private RSAKeyLoader loader;

    public LedgerClientCommand(String username) {
        this.username = username;
        this.loader = RSAKeyLoader.getInstance();
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        try {
            User currentUser = appUser.getUser();
            String endpoint = String.format(defaultUri + PATH, this.username);
            String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);

            LedgerRequest request = HttpUtils.createRequest(appUser.getContractFilePath(),appUser.getKeyId(),loader,(contract, signature)-> new LedgerRequest(contract,signature));
            ResponseEntity<QuorumReplyList> response = HttpUtils.requestWithBody(restTemplate, HttpMethod.POST, endpoint, request, authHeader, QuorumReplyList.class);
            return HttpUtils.processResponse(HttpStatus.OK, response, body -> {
                Set<Transaction> transactions = replyManager.processLedgerClient(body, appUser.getConfig());
                return new LedgerClientCommandResult(transactions);
            });

        } catch (Exception e) {
            return new LedgerClientCommandResult(e.getMessage());
        }
    }
}
