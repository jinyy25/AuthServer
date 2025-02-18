package com.example.authserver.security.jwt;


import com.example.authserver.security.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class TokenProvider {
    
    //Tokne 검증용
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;          //30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;    //30일
    private final Key key;                                                        //Spring security key

    //Token 제공 키생성
    //@Value: springframework.beans.factory.annotation.Value
    //@param secretKey
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes;
        if (secretKey.isEmpty()) {
            // jwt.secret이 비어있으면 무작위 시크릿 키 생성
            keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        } else {
            // Base64 디코딩
            keyBytes = Decoders.BASE64.decode(secretKey);
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //Token 생성
    public TokenDto generateTokenDto (Authentication authentication){

          //권한생성
          String authorities = authentication.getAuthorities().stream()
                              .map(GrantedAuthority::getAuthority)
                              .collect(Collectors.joining(","));


          //만료시간
          long now = (new Date()).getTime();
          Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
          Date refreshExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

          //접근토큰생성
          String accessToken = Jwts.builder()
                  .setSubject(authentication.getName())
                  .claim(AUTHORITIES_KEY, authorities)
                  .setExpiration(tokenExpiresIn)
                  .signWith(key, SignatureAlgorithm.HS512)
                  .compact();

        //refresh 토큰생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(refreshExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();


          //TokenDto 에 생성된 Token 정보 세팅
          return TokenDto.builder()
                 .grantType(BEARER_TYPE)
                 .accessToken(accessToken)
                 .refreshToken(refreshToken)
                 .tokenExpiresIn(tokenExpiresIn.getTime())
                 .build();
      }


    //Token으로 인증요청
    public Authentication getAuthentication(String accessToken) {
        // String accessToken -> Claims
        Claims claims = parseClaims(accessToken);

        //Token에 권한이 있는지 확인
        if(claims.get(AUTHORITIES_KEY)==null){
            throw new RuntimeException("권한정보 없는 토큰");
        }

        //SimpleGrantedAuthority 객체에 인가 존재
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //Claims 객체로 파싱작업
    public Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }catch (JwtException e) { // 수정: JwtException으로 일반적인 JWT 예외 처리
            log.error("JWT 예외 발생: {}", e.getMessage());
            throw new JwtException("JWT 검증 오류");
        }
    }

    //유효성검사
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
               log.info("잘못된 JWT 서명입니다.");
           } catch (ExpiredJwtException e) {
               log.info("만료된 JWT 토큰입니다.");
           } catch (UnsupportedJwtException e) {
               log.info("지원되지 않는 JWT 토큰입니다.");
           } catch (IllegalArgumentException e) {
               log.info("JWT 토큰이 잘못되었습니다.");
           }

        return false;
    }


//    public String validateRefreshToken(RefreshToken refreshTokenObj){
//
//
//            // refresh 객체에서 refreshToken 추출
//            String refreshToken = refreshTokenObj.getRefreshToken();
//
//
//            try {
//                // 검증
//                Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);
//
//                //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성합니다.
//                if (!claims.getBody().getExpiration().before(new Date())) {
//                    return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
//                }
//            }catch (Exception e) {
//
//                //refresh 토큰이 만료되었을 경우, 로그인이 필요합니다.
//                return null;
//
//            }
//
//            return null;
//        }


















}


