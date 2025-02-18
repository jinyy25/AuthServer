package com.example.authserver.security.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/*
    Security Context에 User Info가 저장되는 시점 Handling
*/

public class SecurityUtil {

    private SecurityUtil(){}

    public static String getCurrentMemberId(){
        final Authentication authentication = SecurityContextHolder
                                              .getContext()
                                              .getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }

        return authentication.getName();
    }


}
