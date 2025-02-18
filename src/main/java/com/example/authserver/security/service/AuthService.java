package com.example.authserver.security.service;

import com.example.authserver.member.dto.MemberRequestDto;
import com.example.authserver.member.entity.User;
import com.example.authserver.member.repository.MemberRepository;
import com.example.authserver.security.dto.TokenDto;
import com.example.authserver.security.entity.Auth;
import com.example.authserver.security.jwt.TokenProvider;
import com.example.authserver.security.repository.AuthRepository;
import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service("MemberService")
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private static final String AUTHORITIES_KEY = "auth";
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;
    private final AuthRepository authRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenDto login(MemberRequestDto memberRequestDto ) {
//        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        User user = this.memberRepository.findByUserId(memberRequestDto.getUserId()).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. username = " + memberRequestDto.getUserId()));


        if (!passwordEncoder.matches(memberRequestDto.getUserPw(), user.getUserPw())) {
                 throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. username = " + memberRequestDto.getUserId());
             }

        UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(memberRequestDto.getUserId(),memberRequestDto.getUserPw());
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto =tokenProvider.generateTokenDto(authentication);

        Auth auth = this.authRepository.findByUserId(memberRequestDto.getUserId()).orElseGet(() -> {return null;});

        if (auth != null) {
            auth.setRefreshToken(tokenDto.getRefreshToken());
            this.authRepository.save(auth);
        }else{
            this.authRepository.save(Auth.builder()
                                .refreshToken(tokenDto.getRefreshToken())
                                .userId(memberRequestDto.getUserId())
                                .build());
        }


        return tokenDto;
    }


    public TokenDto refresh(TokenDto requestDto ) {
        if (this.tokenProvider.validateToken(requestDto.getRefreshToken())) {
            Auth auth = this.authRepository.findByRefreshToken(requestDto.getRefreshToken()).orElseThrow(
                    () -> new IllegalArgumentException("해당 REFRESH_TOKEN 을 찾을 수 없습니다.\nREFRESH_TOKEN = " + requestDto.getRefreshToken()));

            Claims claims = tokenProvider.parseClaims(requestDto.getAccessToken());
            Collection<? extends GrantedAuthority> authorities =
                     Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(auth.getUser().getUserId(),"",authorities);
//            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
            TokenDto tokenDto =tokenProvider.generateTokenDto(authenticationToken);

            auth.setRefreshToken(tokenDto.getRefreshToken());
            this.authRepository.save(auth);

            return tokenDto;
        }
        return null;
    }

}
