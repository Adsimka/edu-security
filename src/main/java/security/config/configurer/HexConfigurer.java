package security.config.configurer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import security.config.filter.HexAuthenticationFilter;

public class HexConfigurer extends AbstractHttpConfigurer<HexConfigurer, HttpSecurity> {

    private AuthenticationEntryPoint authenticationEntryPoint;

    public HexConfigurer() {
        authenticationEntryPoint = ((request, response, authException) -> {
            response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Hex");
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        });
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(this.authenticationEntryPoint));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        final var authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(new HexAuthenticationFilter(authenticationManager, authenticationEntryPoint), BasicAuthenticationFilter.class);
    }

    public HexConfigurer authenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        return this;
    }
}
