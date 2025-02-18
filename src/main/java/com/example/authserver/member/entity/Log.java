package com.example.authserver.member.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;



@Getter
@Table(name = "TB_LOG")
@NoArgsConstructor
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logSeq;

    @Column(nullable = false)
    private String vendorSeq;

    @Column(nullable = false)
    private String vendorId;

    @Column(nullable = false)
    private String userSeq;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private int userIp;

    @Column(nullable = false)
    private String created;

    @Column(nullable = false)
    private String logType;

    @Column(nullable = false)
    private String logSearch;




}
