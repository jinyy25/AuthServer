package com.example.authserver.member.dto;



import com.example.authserver.member.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {
    private String userId;
    private Long userSeq;
    private String userName;
    private Date roleDuration;
    private String downType;


    public static MemberResponseDto of(User member) {
        return MemberResponseDto.builder()
                .userId(member.getUserId())
                .userSeq(member.getUserSeq())
                .userName(member.getUserName())
                .roleDuration(member.getRoleDuration())
                .downType(member.getDownType())
                .build();
    }


}
