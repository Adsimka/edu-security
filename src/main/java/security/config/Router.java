package security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route().GET("/api/v4/welcome", request -> {
            UserDetails userDetails = request.principal()
                    .map(Authentication.class::cast)
                    .map(Authentication::getPrincipal)
                    .map(UserDetails.class::cast)
                    .orElseThrow();
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("V4:Welcome, " + userDetails.getUsername() + " to the security configuration");
        }).build();
    }
}
