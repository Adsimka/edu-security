package security.config.converter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.nio.charset.StandardCharsets;

public class HexConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        final var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Hex ")) {
            final var rawToken = header.replaceAll("^Hex ", "");
            final var token = new String(Hex.decode(rawToken), StandardCharsets.UTF_8);
            final var tokenParts = token.split(":");

            return UsernamePasswordAuthenticationToken
                    .unauthenticated(tokenParts[0], tokenParts[1]);
        }
        return null;
    }
}
