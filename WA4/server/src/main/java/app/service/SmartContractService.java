package app.service;

import app.models.User;
import app.models.Wallet;
import app.replication.messages.requests.PermissionProof;
import app.repository.UserRepository;
import app.repository.WalletRepository;
import app.utils.SmartContractUtils;
import bftsmart.tom.util.TOMUtil;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class SmartContractService {

    private static final String IMAGE = "node:10";
    private static final long MAX_MEMORY = 5000000;
    private static final int BLOCK_IO = 0;
    private static final int TIMEOUT = 60000;

    @Value("${server.bftsmart.replica-id}")
    private int replicaId;

    private UserRepository userRepository;
    private WalletRepository walletRepository;

    @Autowired
    public SmartContractService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public boolean execute(PermissionProof proof, OperationPermissions operationPermissions) {
        try {
            if (proof.getContract().isEmpty() || proof.getSignature().isEmpty()) {
                return false;
            }

            User user = userRepository.findByUsername(proof.getUsername());
            PublicKey userPublicKey = getPublicKeyFromString(user.getCertificate());

            String contract = SmartContractUtils.parseContract(proof.getContract());
            byte[] contractBytes = proof.getContract().getBytes();

            if (TOMUtil.verifySignature(userPublicKey, contractBytes, Base64.getDecoder().decode(proof.getSignature()))) {
                Wallet userWallet = walletRepository.findByUsername(user.getUsername());
                float balance = userWallet.getBalance();
                int nTransfers = userWallet.getLedger().size();
                return executeContract(contract, user.getUsername(), balance, nTransfers, operationPermissions.ordinal());
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public enum OperationPermissions {
        BALANCE, DEPOSIT, TRANSFER, LEDGERCLIENT, LEDGERGLOBAL
    }

    private PublicKey getPublicKeyFromString(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(key);
        return keyFactory.generatePublic(publicKeySpec);
    }

    private boolean executeContract(String contract, String username, float balance, int nTransfers, int operation) {
        DockerClient docker = null;
        String id = null;
        try {
            docker = DefaultDockerClient.fromEnv().build();

            docker.pull(IMAGE);
            final HostConfig hostConfig = HostConfig.builder().blkioWeight(BLOCK_IO).memory(MAX_MEMORY).build();
            String validate = "\nconsole.log(validate('%s',%.2f,%d,%d))";

            final ContainerConfig containerConfig = ContainerConfig.builder()
                    .hostConfig(hostConfig)
                    .image(IMAGE)
                    .cmd("sh", "-c", "echo \"" + contract + String.format(validate, username, balance, nTransfers, operation) + "\" > contract.js ; while :; do sleep 1; done")
                    .build();

            final ContainerCreation creation = docker.createContainer(containerConfig);
            id = creation.id();

            docker.startContainer(id);

            DockerClient finalDocker = docker;
            String finalId = id;
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(TIMEOUT);
                    finalDocker.killContainer(finalId);
                } catch (Exception ignored) {
                }
            });


            final String[] command = {"node", "contract.js"};
            t.start();
            final ExecCreation execCreation = docker.execCreate(
                    id, command, DockerClient.ExecCreateParam.attachStdout(),
                    DockerClient.ExecCreateParam.attachStderr());
            final LogStream output = docker.execStart(execCreation.id());
            final String execOutput = output.readFully();
            t.interrupt();
            return execOutput.startsWith("true");

        } catch (Exception e) {
            return false;
        } finally {
            if (docker != null && id != null) {
                try {
                    docker.killContainer(id);
                    docker.removeContainer(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                docker.close();
            }
        }
    }
}
