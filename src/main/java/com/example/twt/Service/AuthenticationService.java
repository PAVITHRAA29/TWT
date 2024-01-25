package com.example.twt.Service;


import com.example.twt.Model.AuthenticationRequest;
import com.example.twt.Model.AuthenticationResponse;
import com.example.twt.Model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
