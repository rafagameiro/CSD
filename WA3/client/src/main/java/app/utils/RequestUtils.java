package app.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RequestUtils {
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
}
