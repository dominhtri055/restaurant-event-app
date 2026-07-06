package nbcc.resto.provider;

import nbcc.auth.config.BearerTokenConfig;
import nbcc.auth.domain.LoginRequest;
import nbcc.auth.service.TokenService;
import nbcc.auth.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;
    private final BearerTokenConfig bearerTokenConfig;

    public UserServiceAuthenticationProvider(TokenService tokenService, BearerTokenConfig bearerTokenConfig) {
        this.tokenService = tokenService;
        this.bearerTokenConfig = bearerTokenConfig;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials() != null ? authentication.getCredentials().toString() : "";

        var result = tokenService.createToken(new LoginRequest(username, password));

        if (result.isError() || result.isEmpty() || !result.isSuccessful()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var bearerToken = result.getValue();
        bearerTokenConfig.setToken(bearerToken.getToken());

        return new UsernamePasswordAuthenticationToken(bearerToken, null, List.of());
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}