package security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/public/login.html").permitAll()
                        .loginProcessingUrl("/login") // URL для обработки username и password (по умл - своей реализации нет)
                        .defaultSuccessUrl("/api/v1/welcome") // ссылка при успешной аутентификации
                )
                .exceptionHandling(exception ->
                        // При попытке зайти на любую страницу, которая защищена аутентификацией, мы будем перенаправлены на /public/error.html
                        exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/public/error.html"))
                )
                .build();
    }
}
