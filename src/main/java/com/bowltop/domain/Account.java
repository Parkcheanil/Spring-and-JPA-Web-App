package com.bowltop.domain;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id") //연관관계가 복잡해질때 @EqualsAndHashCode 에서 서로다른 연관관계를 
                                              //순환참조하느라 무한루프가 발생하고 결국 스택오버플로우가 발생하기때문에 id만 사용
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {
    
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;
    
    private String url;

    private String occupation;

    private String location;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean studyCreatedByEmail;
    
    private boolean studyCreatedByWeb;
    
    private boolean studyEnrollmentResultByEmail;
    
    private boolean studyEnrollmentResultByWeb;
    
    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;
    

}