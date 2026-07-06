package nbcc.resto.config;

import jakarta.servlet.http.HttpSession;
import nbcc.auth.config.BearerTokenConfig;
import org.springframework.stereotype.Component;

@Component
public class BearerTokenConfigImpl implements BearerTokenConfig {

    private final HttpSession session;

    public static final String JWT_SESSION_KEY = "JWT_TOKEN";

    public BearerTokenConfigImpl(HttpSession session) {
        this.session = session;
    }

    @Override
    public String getToken() {
        var attribute = session.getAttribute(JWT_SESSION_KEY);
        return attribute != null ? session.getAttribute(JWT_SESSION_KEY).toString() : null;
    }

    @Override
    public void setToken(String token) {
        session.setAttribute(JWT_SESSION_KEY, token);
    }
}
