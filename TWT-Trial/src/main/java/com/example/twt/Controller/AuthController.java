package com.example.twt.Controller;


import com.example.twt.DTO.ResetPasswordDTO;
import com.example.twt.DTO.AuthenticationRequest;
import com.example.twt.DTO.AuthenticationResponse;
import com.example.twt.DTO.RegisterRequest;
import com.example.twt.Model.twtUser;
import com.example.twt.Repo.twtUserRepository;
import com.example.twt.Service.AuthenticationService;
import com.example.twt.Service.JwtService;
import com.example.twt.Service.twtUserService;
import com.example.twt.api.AuthApi;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    @Autowired
    private final AuthenticationService service;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final twtUserService twtUserService;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Autowired
    private final twtUserRepository userRepository;

    @Override
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @Override
    public ResponseEntity<String> verifyEmail(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                String username = jwtService.extractUserName(jwtToken);
                twtUser user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

                if (jwtService.isTokenValid(jwtToken, user)) {
                    userRepository.save(user);
                    return ResponseEntity.ok("Email verified successfully!");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying email");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying email");
    }

    @Override
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String refreshToken = authorizationHeader.substring(7);
                String newAccessToken = jwtService.refreshAccessToken(refreshToken);
                return ResponseEntity.ok(newAccessToken);
            } else {
                return ResponseEntity.badRequest().body("Invalid Authorization header");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error refreshing token");
        }
    }

    @Override
    public ResponseEntity<twtUser> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                String username = jwtService.extractUserName(jwtToken);
                UserDetails userDetails = twtUserService.loadUserByUsername(username);
                if (userDetails != null) {
                    twtUser user = extractUserFromUserDetails(userDetails);
                    return ResponseEntity.ok(user);
                }
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<String> forgotPassword(@RequestBody RegisterRequest registerRequest) {
        try {
            String email = registerRequest.getEmail();
            twtUser user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            String resetCode = generateResetCode();
            user.setResetPasswordCode(resetCode);
            sendResetPasswordEmail(email, resetCode);

            userRepository.save(user);
            return ResponseEntity.ok("Verification code sent to your email.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing forgot password request.");
        }
    }

    @Override
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            String email = resetPasswordDTO.getEmail();
            String code = resetPasswordDTO.getCode();
            String newPassword = resetPasswordDTO.getNewpassword();
            twtUser user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            if (code.equals(user.getResetPasswordCode())) {
                user.setPassword(passwordEncoder.encode(newPassword));
//                user.setResetPasswordCode(null);
                userRepository.save(user);
                return ResponseEntity.ok("Password reset successful.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid verification code.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password.");
        }
    }



    private twtUser extractUserFromUserDetails(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userDetails.getUsername()));
    }

    private String generateResetCode() {
        Random random = new SecureRandom();
        int codeLength = 6;
        StringBuilder resetCode = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int digit = random.nextInt(10);
            resetCode.append(digit);
        }
        return resetCode.toString();
    }

    private void sendResetPasswordEmail(String email, String resetCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Reset Your Password");
            mailMessage.setText("Use the following code to reset your password: " + resetCode);

            javaMailSender.send(mailMessage);
            System.out.println("Reset password email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error sending reset password email.");
        }
    }

}