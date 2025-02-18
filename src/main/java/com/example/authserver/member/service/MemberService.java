package com.example.authserver.member.service;


import com.example.authserver.member.dto.LoginHistoryDto;
import com.example.authserver.member.dto.MemberInfoDto;
import com.example.authserver.member.dto.MemberResponseDto;
import com.example.authserver.member.mapper.MemberMapper;
import com.example.authserver.member.repository.LogRepository;
import com.example.authserver.member.repository.MemberRepository;
import com.example.authserver.security.config.SecurityUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;
    private final MemberMapper mapper;

    public MemberResponseDto getMyInfoBySecurity(HttpServletRequest request) {
        MemberResponseDto myInfoBySecurity  = memberRepository.findByUserId(SecurityUtil.getCurrentMemberId())
                        .map(MemberResponseDto::of)
                        .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        String userIp = getClientIp(request);
        LoginHistoryDto loginHistoryDto = new LoginHistoryDto();
        loginHistoryDto.setLoginId(myInfoBySecurity.getUserId());
        loginHistoryDto.setUserSeq(myInfoBySecurity.getUserSeq());
        loginHistoryDto.setUserIp(userIp);

        int insert = insertLoginHistory(loginHistoryDto);
        int result = updateLoginHistory(myInfoBySecurity.getUserSeq());


        return myInfoBySecurity;
    }




    public int insertLoginHistory(LoginHistoryDto loginHistoryDto){
        return mapper.insertLoginHistory(loginHistoryDto);
    }

    public int updateLoginHistory(Long userSeq){
        return mapper.updateLoginHistory(userSeq);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    public MemberInfoDto getUserInfo(MemberInfoDto memberInfoDto) {
        return mapper.getUserInfo(memberInfoDto);
    }




}
