package security.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

    @GetMapping("/api/v1/welcome")
    public String welcomeV1() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return "V1:Welcome, " + userDetails.getUsername();
    }

    @GetMapping("/api/v2/welcome")
    public String welcomeV2(HttpServletRequest request) {
        UserDetails userDetails = (UserDetails) ((Authentication) request.getUserPrincipal()).getPrincipal();
        return "V2:Welcome, " + userDetails.getUsername();
    }

    @GetMapping("/api/v3/welcome")
    public String welcomeV3(@AuthenticationPrincipal UserDetails userDetails) {
        return "V3:Welcome, " + userDetails.getUsername();
    }
}
