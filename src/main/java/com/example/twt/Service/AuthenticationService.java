package com.example.twt.Service;


import com.example.twt.DTO.AuthenticationRequest;
import com.example.twt.DTO.AuthenticationResponse;
import com.example.twt.DTO.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
