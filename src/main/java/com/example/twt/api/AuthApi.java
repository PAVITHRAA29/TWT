package com.example.twt.api;


import com.example.twt.Model.AuthenticationRequest;
import com.example.twt.Model.AuthenticationResponse;
import com.example.twt.Model.RegisterRequest;
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
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token);

    @PostMapping("forgot-password/")
    public ResponseEntity<String> forgotPassword(@RequestParam String email);

    @PostMapping("reset-password/")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword);


}
