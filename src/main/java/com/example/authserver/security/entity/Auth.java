package com.example.authserver.security.entity;


import com.example.authserver.member.entity.User;
import lombok.*;

import javax.persistence.*;


@Builder
@Entity
@Getter
@Setter
@Table(name = "TB_AUTH")
@NoArgsConstructor
@AllArgsConstructor
public class Auth {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID", nullable = false)
    private Long tokenId;

    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String refreshToken;

    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

//    @Column(name = "TOKEN_TYPE", nullable = false)
//    private String tokenType;

    @Column(name = "USER_ID")
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "userId", insertable = false, updatable = false)
    private User user;

    @Builder
    public Auth(User user, String userId, String accessToken, String refreshToken) {
        this.user = user;
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
