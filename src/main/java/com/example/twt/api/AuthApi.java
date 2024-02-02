package com.example.twt.api;


import com.example.twt.DTO.ResetPasswordDTO;
import com.example.twt.DTO.AuthenticationRequest;
import com.example.twt.DTO.AuthenticationResponse;
import com.example.twt.DTO.RegisterRequest;
import com.example.twt.Model.twtUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/api/auth/")
public interface AuthApi {

    @PostMapping("register/")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request);

    @PostMapping("login/")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request);

    @GetMapping("details/")
    public ResponseEntity<twtUser> getUserDetails(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("verify/")
    public ResponseEntity<String> verifyEmail(@RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("forgot-password/")
    public ResponseEntity<String> forgotPassword(@RequestBody RegisterRequest registerRequest);

    @PostMapping("reset-password/")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO);

    @PostMapping("refresh-token/")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorizationHeader);


}
