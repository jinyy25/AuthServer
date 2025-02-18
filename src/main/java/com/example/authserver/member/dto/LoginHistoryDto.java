package com.example.authserver.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryDto {
    String loginId;
    String userIp;
    Long userSeq;
}
