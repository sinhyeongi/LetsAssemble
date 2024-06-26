package com.la.letsassemble.Entity;

import com.la.letsassemble.Role.UsersRole;
import com.la.letsassemble.dto.UserForm;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;


@Entity
@Data
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    private String provider; // 소셜타입
    private String providerId;

    @Column(nullable = false)
    private String lastLogin; // 마지막 로그인 날

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
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
    }
    public static Users changePassword(Users user,String password){
        user.password = password;
        return user;
    }
    public static Users changeNickname(Users user,String nickname){
        user.nickname = nickname;
        return user;
    }

    public static Users createUser(UserForm form , BCryptPasswordEncoder encoder ){
        Users users = new Users();
        users.email = form.getEmail();
        users.nickname = form.getNickname();
        if(form.getProvider() != null){
            form.setPassword(generateRandomString(new Random().nextInt(9)+1));
            System.out.println("생성 비밀번호  = " + form.getPassword());
        }
        users.password = encoder.encode(form.getPassword());
        users.name = form.getName();
        users.phone = form.getPhone();
        users.gender = form.getGender();
        users.age = form.getAge();
        users.provider = form.getProvider();
        users.providerId = form.getProviderId();
        return users;
    }

    public static String generateRandomString(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
