package com.example.study.security;

import com.example.study.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5"; // 시크릿 키

    public String create(UserEntity userEntity){ // 토큰 생성 메소드
        // 기한은 지금부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));
        // JWT Token생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();

    }
    public String validateAndGetUserId(String token){
        // (1) 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims)리턴, 위조되면 예외를 날림
        // 그중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser() // payload정보를 담는 객체
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)// (1)
                .getBody();
        return claims.getSubject(); // userEntity의 id를 얻음
    }


}












