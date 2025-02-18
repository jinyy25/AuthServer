package com.example.authserver.security.service;

import com.example.authserver.member.entity.User;
import com.example.authserver.member.mapper.MemberMapper;
import com.example.authserver.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return memberRepository.findByUserId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " 을 DB에서 찾을 수 없습니다" ));
    }


    private UserDetails createUserDetails(User member){
        int update = 0;
        if(member.getDownType().equals("ROLE_ALL")||member.getDownType().equals("ROLE_DOWNLOAD")){
            update = updateRoleDuration(member.getRoleDuration(),member.getUserId());
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getUserType()));
        authorities.add(new SimpleGrantedAuthority(member.getManType()));

        if(update == 1) authorities.add(new SimpleGrantedAuthority("NONE"));
        else authorities.add(new SimpleGrantedAuthority(member.getDownType()));

        return new org.springframework.security.core.userdetails.User(
            String.valueOf(member.getUserId()),
            member.getUserPw(),
            authorities
        );
    }

    public int updateRoleDuration(Date givenDate, String userId){
        LocalDate currentDate = LocalDate.now();
        LocalDate convertedGivenDate = givenDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 날짜 차이 계산
        long difference = ChronoUnit.DAYS.between(convertedGivenDate, currentDate);

        // 날짜 차이 계산 후 절대값 취할 필요 없이 바로 3일과 비교
        if (difference >= 3) {
            System.out.println("주어진 날짜와 현재 날짜 간의 차이는 3일 입니다.");
            return mapper.updateRoleSetting(userId);
        } else {
            System.out.println("주어진 날짜와 현재 날짜 간의 차이는 3일이 아닙니다.");
            return 0;
        }

    }




}
