package nbcc.resto.config;

import nbcc.resto.provider.UserServiceAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserServiceAuthenticationProvider userServiceAuthenticationProvider;

    public SecurityConfig(UserServiceAuthenticationProvider userServiceAuthenticationProvider) {
        this.userServiceAuthenticationProvider = userServiceAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) {
        http
                .authenticationProvider(userServiceAuthenticationProvider)
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureHandler((request, response, exception) -> {
                            String username = request.getParameter("username");
                            response.sendRedirect("/login?error=true&username=" +
                                    java.net.URLEncoder.encode(username != null ? username : "",
                                            java.nio.charset.StandardCharsets.UTF_8));
                        })
                        .permitAll()
                );

        return http.build();
    }
}
