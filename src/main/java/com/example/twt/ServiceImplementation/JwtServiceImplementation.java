package com.example.twt.ServiceImplementation;

import com.example.twt.Model.twtUser;
import com.example.twt.Service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtServiceImplementation implements JwtService {

    private static final String SECRET_KEY = "5f787bf595208ce61a0105d4e8762643adaaeda1fac8202a7f6b410492ac02a8";

    @Override
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }


    @Override
    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode((SECRET_KEY));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> ClaimsResolver) {
        final Claims claims = extractAllClaims(token);
        return ClaimsResolver.apply(claims);
    }


    @Override
    public String generateToken(twtUser user) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", user.getUserid());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getJwtToken(twtUser user) {
        return generateToken(user);
    }


    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public String getRefreshToken(twtUser twtUser){
        return Jwts.builder()
                .setSubject(twtUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*60*24*7)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        Claims claims = extractAllClaims(refreshToken);
        Date expiration = claims.getExpiration();

        if (expiration.before(new Date(System.currentTimeMillis()))) {
            throw new RuntimeException("Refresh token has expired");
        }
        return generateTokenFromRefreshToken(refreshToken);
    }


    private String generateTokenFromRefreshToken(String refreshToken) {
        String username = extractUserName(refreshToken);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*60*24)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
