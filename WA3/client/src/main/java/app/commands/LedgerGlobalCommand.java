package app.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import app.commandresults.CommandResult;
import app.commandresults.LedgerGlobalCommandResult;
import app.models.Transaction;
import app.models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import app.utils.RequestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class LedgerGlobalCommand implements Command {
    public static final String HELP = "ledgerglobal";
    private static final String PATH = "/wallets/ledger";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        String endpoint = defaultUri + PATH;
        String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);
        ResponseEntity<String> response = RequestUtils.request(restTemplate, HttpMethod.GET, endpoint, authHeader, String.class);

        Gson gson = new Gson();
        Set<Transaction> ledger = gson.fromJson(response.getBody(),
                new TypeToken<Set<Transaction>>() {
                }.getType());

        return new LedgerGlobalCommandResult(ledger);
    }
}
