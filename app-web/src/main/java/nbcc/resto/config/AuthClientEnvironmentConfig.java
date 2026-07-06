package nbcc.resto.config;

import nbcc.auth.config.AuthClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AuthClientEnvironmentConfig implements AuthClientConfig {

    private final Environment environment;

    public AuthClientEnvironmentConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getBaseUrl() {
        return environment.getProperty("auth.rest.api.url");
    }

    @Override
    public String getPort() {
        return environment.getProperty("auth.rest.api.port");
    }
}
