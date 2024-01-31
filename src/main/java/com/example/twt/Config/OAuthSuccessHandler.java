package com.example.twt.Config;


import com.example.twt.Model.twtUser;
import com.example.twt.Repo.twtUserRepository;
import com.example.twt.Service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Component
@Configuration
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final twtUserRepository userRepository;
    private final JwtService jwtService;


    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String email = ((DefaultOAuth2User)((OAuth2AuthenticationToken)authentication).getPrincipal()).getAttributes().getOrDefault("email","").toString();
        twtUser employeeEntity = userRepository.findByEmail(email)
                .orElseThrow();
        System.out.println(email);
        if(employeeEntity==null){
            response.sendRedirect("http://localhost:8080/usernotfound");
        }
        else{
            Cookie authToken = new Cookie("auth_token", jwtService.getJwtToken(employeeEntity));
            Cookie refToken = new Cookie("refresh_token", jwtService.getRefreshToken(employeeEntity));
            authToken.setPath("/");
            refToken.setPath("/");
            response.addCookie(authToken);
            response.addCookie(refToken);
            response.sendRedirect(frontendUrl);
        }

    }

}
