package security.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HexAuthenticationFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    private AuthenticationEntryPoint authenticationEntryPoint;

    private SecurityContextHolderStrategy securityContextHolderStrategy;

    private SecurityContextRepository securityContextRepository;

    public HexAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        this.securityContextRepository = new RequestAttributeSecurityContextRepository();
    }

    /**
     * Custom filter by 'Hex' headers
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Hex ")) {
            final var rawToken = header.replaceAll("^Hex ", "");
            final var token = new String(Hex.decode(rawToken), StandardCharsets.UTF_8);
            final var tokenParts = token.split(":");

            final var authenticationRequest = UsernamePasswordAuthenticationToken
                    .unauthenticated(tokenParts[0], tokenParts[1]);
            try {
                final var authentication = authenticationManager.authenticate(authenticationRequest);
                final var context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                this.securityContextHolderStrategy.setContext(context);
                this.securityContextRepository.saveContext(context, request, response);
            } catch (AuthenticationException exception) {
                this.securityContextHolderStrategy.clearContext();
                this.authenticationEntryPoint.commence(request, response, exception);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
