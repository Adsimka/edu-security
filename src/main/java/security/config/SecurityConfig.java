package security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import security.config.configurer.HexConfigurer;
import security.service.JdbcUserDetailsService;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    /**
     * Custom hex authentication filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated()
                        .requestMatchers("/error").permitAll())
                .with(new HexConfigurer(), Customizer.withDefaults()).build();
    }

    /**
     * Custom simple filter
     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .addFilterBefore(new DeniedClientFilter(), DisableEncodeUrlFilter.class)
//                .httpBasic(Customizer.withDefaults())
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
//                .build();
//    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsService(dataSource);
    }
}
