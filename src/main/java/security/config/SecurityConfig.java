package security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import security.service.JdbcUserDetailsService;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class SecurityConfig {

    /**
     *  Custom configurer
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("AuthenticationManager in config: {}", http.getSharedObject(AuthenticationManager.class));
        http.apply(new CustomSecurityConfigurer())
                .realmName("Custom realm name");
        return http.build();

    }

    /**
     *  Basic authenticated
     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .httpBasic(Customizer.withDefaults())
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
//                .build();
//    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsService(dataSource);
    }
}
