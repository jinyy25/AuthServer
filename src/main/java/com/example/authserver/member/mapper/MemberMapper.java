package com.example.authserver.member.mapper;


import com.example.authserver.member.dto.LoginHistoryDto;
import com.example.authserver.member.dto.MemberInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberMapper {

    int insertLoginHistory(LoginHistoryDto loginHistoryDto);
    int updateLoginHistory(Long userSeq);
    MemberInfoDto getUserInfo(MemberInfoDto memberInfoDto);
    int updateRoleSetting(String userId);
}
