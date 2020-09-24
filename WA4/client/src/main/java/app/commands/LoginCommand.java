package app.commands;

import app.CommandLineApp;
import app.commandresults.CommandResult;
import app.commandresults.LoginCommandResult;
import app.commandresults.ReplyManager;
import app.models.AppUser;
import app.models.requests.LoginRequest;
import app.models.User;
import app.utils.RSAKeyLoader;
import app.utils.HttpUtils;
import app.utils.SmartContractUtils;
import app.utils.TOMUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginCommand implements Command {
    public static final String COMMAND = "login";
    public static final String USAGE = "login <username> <password> <key-id>";
    public static final String[] OPT_DESCRIPTION = {"account username", "account password","ID of public key used in registration process"};
    public static final String DESCRIPTION = "Logs in the user into the system.";
    public static final String HELP = " login\t\t\t\t\t"+DESCRIPTION;
    private static final String PATH = "/login";

    private User user;
    private int keyId;
    private RSAKeyLoader loader;

    public LoginCommand(String username, String password, String keyId) {
        this.user = new User(username, password);
        this.keyId = Integer.parseInt(keyId);
        loader = RSAKeyLoader.getInstance();
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        try {
            String endpoint = defaultUri + PATH;
            String authHeader = HttpHeaders.encodeBasicAuth(user.getUsername(), user.getPassword(), StandardCharsets.UTF_8);

            this.user.setCertificate(loader.loadPublicKey(keyId).getEncoded());

            LoginRequest request = HttpUtils.createRequest(appUser.getContractFilePath(),appUser.getKeyId(),loader,(contract,signature)-> new LoginRequest(contract,signature));
            ResponseEntity<String> response = HttpUtils.requestWithBody(restTemplate, HttpMethod.POST, endpoint, request, authHeader, String.class);

            boolean success = response.getStatusCode().equals(HttpStatus.OK);
            if (success) {
                appUser.setUser(user);
                appUser.setKeyId(keyId);
            }
            return new LoginCommandResult(success);
        } catch (Exception e) {
            return new LoginCommandResult(false);
        }
    }
}
