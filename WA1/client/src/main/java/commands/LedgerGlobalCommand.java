package commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commandresults.CommandResult;
import commandresults.LedgerGlobalCommandResult;
import models.Transaction;
import models.UserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utils.RequestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class LedgerGlobalCommand implements Command {
    public static final String HELP = "ledgerglobal";
    private static final String PATH = "/wallets/ledger";

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, UserModel currentUser) {
        String endpoint = defaultUri + PATH;
        String authHeader = HttpHeaders.encodeBasicAuth(currentUser.getUsername(), currentUser.getPassword(), StandardCharsets.UTF_8);
        ResponseEntity<String> response = RequestUtils.request(restTemplate, HttpMethod.GET, endpoint, authHeader, String.class);

        Gson gson = new Gson();
        List<Transaction> ledger = gson.fromJson(response.getBody(),
                new TypeToken<List<Transaction>>() {
                }.getType());

        return new LedgerGlobalCommandResult(ledger);
    }
}
