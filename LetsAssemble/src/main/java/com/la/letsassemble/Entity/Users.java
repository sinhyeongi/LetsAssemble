package com.la.letsassemble.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String email; // 이메일 유저 아이디 값
    @Column(nullable = false)
    private String password; //비밀번호
    @Column(nullable = false)
    private String phone; // 휴대폰 번호
    @Column(nullable = false)
    private String name; // 이름
    @Column(nullable = false,unique = true, name = "nick_name")
    private String nickname; // 닉네임
    @CreationTimestamp
    @Column(nullable = false)
    private String lastLogin; // 마지막 로그인 날
    @ColumnDefault("0")
    private int suspensionPeriod; // 정지 기간
    @ColumnDefault("0")
    private int point; // 포인트
    @Column(nullable = false)
    private String gender; // 성별
    @Column(nullable = false)
    private int age; // 나이

    //Build 작성
    @Builder
    public Users(String email,String password,String phone,String name,String nickname,String gender,int age){
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
    }
}
