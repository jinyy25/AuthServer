package com.example.authserver.member.controller;


import com.example.authserver.member.dto.MemberInfoDto;
import com.example.authserver.member.dto.MemberResponseDto;
import com.example.authserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/users/login")
    public ResponseEntity<MemberResponseDto> login(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.getMyInfoBySecurity(request));
      }


    @PostMapping("/api/users/profile")
    public MemberInfoDto getUserInfo(@RequestBody MemberInfoDto memberInfoDto) {
     return memberService.getUserInfo(memberInfoDto);
    }
}
