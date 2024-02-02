package com.example.twt.Service;

import com.example.twt.Model.twtUser;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Service
public interface JwtService {

    String extractUserName(String token);

    Claims extractAllClaims(String token);

    Key getSignInKey();

    <T> T extractClaim(String token, Function<Claims,T> ClaimsResolver);

//    String generateToken(
//            Map<String,Object> extraClaims,
//            UserDetails userDetails
//    );

    String generateToken(twtUser user);

    String getJwtToken(twtUser user);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    String getRefreshToken(twtUser twtUser);

    public String refreshAccessToken(String refreshToken);


}
