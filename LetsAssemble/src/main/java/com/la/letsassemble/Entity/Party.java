package com.la.letsassemble.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Data
@NoArgsConstructor
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host",referencedColumnName = "email")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user; // 파티장 / 파티 생성자
    @Column(nullable = false)
    private String title; //파티 이름
    @Column(nullable = false)
    private Boolean isOnline; //온라인 여부
    @Column(nullable = false)
    private String interest; // 관심사 ex) 공부,게임 등
    private String area; // 지역
    @Column(nullable = false)
    private String content; // 모집글 내용
    @CreationTimestamp
    private String riterDay; // 생성일
    @Column(nullable = false)
    private int personnel; // 모집인원
    private String notification; // 전체 공지
}
