package app.commands;

import app.CommandLineApp;
import app.commandresults.CommandResult;
import app.commandresults.RegisterUserCommandResult;
import app.commandresults.ReplyManager;
import app.models.AppUser;
import app.models.QuorumReplyList;
import app.models.User;
import app.models.requests.CreateUserRequest;
import app.utils.HttpUtils;
import app.utils.RSAKeyLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RegisterUserCommand implements Command {
    public static final String COMMAND = "register";
    public static final String USAGE = "register <username> <password> <key-id>";
    public static final String[] OPT_DESCRIPTION = {"account username", "account password","ID of public key"};
    public static final String DESCRIPTION = "Creates a new account.";
    public static final String HELP = " register\t\t\t\t"+DESCRIPTION;
    private static final String PATH = "/users";

    private User user;
    private int keyId;
    private RSAKeyLoader loader;

    public RegisterUserCommand(String username, String password, String keyId) {
        this.user = new User(username, password);
        this.keyId = Integer.parseInt(keyId);
        loader=RSAKeyLoader.getInstance();
    }

    @Override
    public CommandResult execute(String defaultUri, RestTemplate restTemplate, AppUser appUser, ReplyManager replyManager) {
        try {

            this.user.setCertificate(loader.loadPublicKey(keyId).getEncoded());
            CreateUserRequest request = new CreateUserRequest(user);

            String endpoint = defaultUri + PATH;
            ResponseEntity<QuorumReplyList> response = HttpUtils.requestWithBody(restTemplate, HttpMethod.POST, endpoint, request, null, QuorumReplyList.class);
            return HttpUtils.processResponse(HttpStatus.CREATED, response, body ->{
                replyManager.processCreateUser(body, appUser.getConfig());
                appUser.setUser(user);
                appUser.setKeyId(keyId);
                return new RegisterUserCommandResult();
            } );

        } catch (Exception e) {
            return new RegisterUserCommandResult(e.getMessage());
        }
    }
}