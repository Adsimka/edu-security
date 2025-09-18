package security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Slf4j
public class CustomSecurityConfigurer extends AbstractHttpConfigurer<CustomSecurityConfigurer, HttpSecurity> {

    private String realmName = "Basic realm";

    @Override
    public void init(final HttpSecurity http) throws Exception {
        log.info("AuthenticationManager in init: {}", http.getSharedObject(AuthenticationManager.class));
        http.httpBasic(basic -> basic.realmName(realmName))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("AuthenticationManager in configurer: {}", http.getSharedObject(AuthenticationManager.class));

    }

    public CustomSecurityConfigurer realmName(String realmName) {
        this.realmName = realmName;
        return this;
    }
}
