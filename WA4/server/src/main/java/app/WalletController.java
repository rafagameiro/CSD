package app;

import app.models.QuorumReplyList;
import app.models.requests.*;
import app.replication.messages.requests.PermissionProof;
import app.service.SmartContractService;
import app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class WalletController {
    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping(path = {"/test"})
    public String test() {
        return "welcome to the worlds most famous e-wallet!";
    }

    @PostMapping(path = {"/users"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public DeferredResult<ResponseEntity<QuorumReplyList>> createUser(@RequestBody CreateUserRequest request) {
        DeferredResult<ResponseEntity<QuorumReplyList>> response = new DeferredResult<>();
        walletService.createUser(request.getUser(), replyList -> response.setResult(ResponseEntity.status(HttpStatus.CREATED).body(replyList)));
        return response;
    }

    @PostMapping(path = {"/login"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity login() {
        return ResponseEntity.ok().build();

    }

    @PostMapping(path = {"/wallets/{u}"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public DeferredResult<ResponseEntity<QuorumReplyList>> balance(@PathVariable("u") String username, @RequestBody BalanceRequest request) {
        DeferredResult<ResponseEntity<QuorumReplyList>> response = new DeferredResult<>();
        PermissionProof proof = new PermissionProof(username, request.getContract(), request.getSignature());
        walletService.balance(proof, replies -> response.setResult(ResponseEntity.ok(replies)));

        return response;
    }

    @PutMapping(path = {"/wallets/{u}"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public DeferredResult<ResponseEntity<QuorumReplyList>> deposit(@PathVariable("u") String username, @RequestBody DepositRequest request) {
        DeferredResult<ResponseEntity<QuorumReplyList>> response = new DeferredResult<>();
        PermissionProof proof = new PermissionProof(username, request.getContract(), request.getSignature());
        walletService.deposit(proof, request.getAmount(), replies -> response.setResult(ResponseEntity.ok(replies)));

        return response;
    }

    @PutMapping(path = {"/wallets/{u}/ledger"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public DeferredResult<ResponseEntity<QuorumReplyList>> transfer(@PathVariable("u") String username, @RequestBody TransferRequest request) {
        DeferredResult<ResponseEntity<QuorumReplyList>> response = new DeferredResult<>();
        PermissionProof proof = new PermissionProof(username, request.getContract(), request.getSignature());
        walletService.transfer(proof, request.getTransaction(), replies -> response.setResult(ResponseEntity.ok(replies)));

        return response;
    }

    @PostMapping(path = {"/wallets/{u}/ledger"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public DeferredResult<ResponseEntity<QuorumReplyList>> getClientLedger(@PathVariable("u") String client, @RequestBody LedgerRequest request, @RequestAttribute("user") String username) {
        DeferredResult<ResponseEntity<QuorumReplyList>> response = new DeferredResult<>();
        PermissionProof proof = new PermissionProof(username, request.getContract(), request.getSignature());
        walletService.getLedger(proof, client, ledger -> response.setResult(ResponseEntity.ok(ledger)));

        return response;
    }

    @PostMapping(path = {"/wallets/ledger"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public DeferredResult<ResponseEntity<QuorumReplyList>> getGlobalLedger(@RequestAttribute("user") String username, @RequestBody LedgerRequest request) {
        DeferredResult<ResponseEntity<QuorumReplyList>> response = new DeferredResult<>();
        PermissionProof proof = new PermissionProof(username, request.getContract(), request.getSignature());
        walletService.getLedger(proof, ledger -> response.setResult(ResponseEntity.ok(ledger)));

        return response;
    }
}
