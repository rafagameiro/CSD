package g2.labs.interceptors;

import g2.labs.service.WalletService;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Component
public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    private static final String SEPARATOR = ":";
    private static final String USER_ATTRIBUTE = "user";
    private WalletService walletService;

    @Autowired
    public BasicAuthenticationInterceptor(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (intercept(request.getRequestURI())) {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            String[] decoded = new String(Base64.getDecoder().decode(authorization)).split(SEPARATOR);
            String username = decoded[0];
            String password = decoded[1];
            request.setAttribute(USER_ATTRIBUTE, username);
            if (!walletService.verifyUser(username, password)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    private boolean intercept(String uri) {
        return !(uri.endsWith("/users") || uri.endsWith("/test"));
    }
}
