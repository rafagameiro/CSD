package app.utils;

import app.exceptions.ServerErrorException;
import app.exceptions.SmartContractException;
import app.exceptions.UnauthorizedException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class HttpUtils {

    public static <T, V> ResponseEntity<T> requestWithBody(RestTemplate restTemplate, HttpMethod method, String endpoint,
                                                           V body, String auth, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (auth != null) {
            headers.set(HttpHeaders.AUTHORIZATION, auth);
        }
        HttpEntity<V> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(endpoint, method, entity, responseType);
    }

    public static <T> ResponseEntity<T> request(RestTemplate restTemplate, HttpMethod method, String endpoint,
                                                String auth, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (auth != null) {
            headers.set(HttpHeaders.AUTHORIZATION, auth);
        }
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(endpoint, method, entity, responseType);
    }

    public static <T> T createRequest(String contractFilePath, int keyId, RSAKeyLoader loader, Function2<String, String, T> constructor) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, SmartContractException {
        if (contractFilePath != null) {
            String contract = SmartContractUtils.loadContract(contractFilePath);
            byte[] signature = TOMUtil.signMessage(loader.loadPrivateKey(keyId), contract.getBytes());
            return constructor.apply(contract, Base64.getEncoder().encodeToString(signature));
        } else {
            return constructor.apply("", "");
        }
    }

    public static <T, V> T processResponse(HttpStatus expected, ResponseEntity<V> response, Function1<V, T> onSuccess) throws Exception {
        if (response.getStatusCode().equals(expected) && response.hasBody()) {
            return onSuccess.apply(response.getBody());
        } else {
            throw new ServerErrorException();
        }
    }

}
