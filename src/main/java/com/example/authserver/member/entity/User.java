package com.example.authserver.member.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Builder
@Entity
@Getter
@Table(name = "TB_USERS")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userType;

    @Column(nullable = false)
    private int userStatus;

    @Column(nullable = false)
    private String manType;

    @Column(nullable = false)
    private String downType;

    @Column(nullable = false)
    private Date roleDuration;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
//    private Auth auth;

    public void setUserPw(String password) { this.userPw = userPw; }


    @Builder
    public User(Long userSeq,String userId, String userPw
    ,String userName, String userType, String manType, String downType, Date roleDuration
    ) {
        this.userSeq = userSeq;
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userType = userType;
        this.manType = manType;
        this.downType = downType;
        this.roleDuration = roleDuration;

    }




}
