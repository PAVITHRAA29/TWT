package com.example.twt.ServiceImplementation;

import com.example.twt.DTO.AuthenticationRequest;
import com.example.twt.DTO.AuthenticationResponse;
import com.example.twt.DTO.RegisterRequest;
import com.example.twt.Model.twtUser;
import com.example.twt.Repo.twtUserRepository;
import com.example.twt.Service.AuthenticationService;
import com.example.twt.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final twtUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = twtUser.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .bio(request.getBio())
                .username(request.getUsername())
                .build();
        String emailVerificationToken = jwtService.generateToken(user);
        user.setEmailVerificationToken(emailVerificationToken);
        sendVerificationEmail(user.getEmail(), emailVerificationToken);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.getRefreshToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user =  repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.getRefreshToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void sendVerificationEmail(String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Verify your email");
        mailMessage.setText("Click the link to verify your email: http://localhost:8080/verify?token=" + token);

        javaMailSender.send(mailMessage);
    }
}
