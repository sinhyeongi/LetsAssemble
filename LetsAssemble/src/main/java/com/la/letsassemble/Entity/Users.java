package com.la.letsassemble.Entity;

import com.la.letsassemble.Role.UsersRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


import java.time.LocalDate;



@Entity(name="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private String type; // 소셜타입
 
    @Column(nullable = false)
    private String lastLogin; // 마지막 로그인 날
    @ColumnDefault("0")
    private String suspensionPeriod; // 정지 기간
    @ColumnDefault("0")
    private int point; // 포인트
    @Column(nullable = false)
    private String gender; // 성별
    @Column(nullable = false)
    private int age; // 나이
    @Enumerated(EnumType.STRING)
    private UsersRole role;
    @PrePersist
    private void prepersist(){
        this.lastLogin = LocalDate.now().toString();
        this.role = UsersRole.ROLE_USER;
    }
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
