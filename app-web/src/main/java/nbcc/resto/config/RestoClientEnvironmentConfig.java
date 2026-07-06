package nbcc.resto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RestoClientEnvironmentConfig implements RestoClientConfig{

    private final Environment environment;
    public RestoClientEnvironmentConfig(Environment environment) {
        this.environment = environment;
    }


    @Override
    public String getBaseUrl() {
        return environment.getProperty("resto.rest.api.url");
    }

    @Override
    public String getPort() {
        return environment.getProperty("resto.rest.api.port");
    }
}
