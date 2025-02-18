package com.example.authserver.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {
    private String userId;
    private String userName;
    private int mileage;
    private String userType;
    private String manType;
    private String downType;
    private String roleDuration;
}
