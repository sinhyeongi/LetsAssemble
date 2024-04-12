package com.la.letsassemble.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "host",referencedColumnName = "email")
    private Users user;// 파티장 / 파티 생성자
    @Column(nullable = false)
    private String title; //파티 이름
    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    private boolean isOnline; //온라인 여부
    @Column(nullable = false)
    private String interest; // 관심사 ex) 공부,게임 등
    private String area; // 지역
    private String content; // 모집글 내용
    @Column(nullable = false)
    private String riterDay; // 생성일
    @Column(nullable = false)
    private int personnel; // 모집인원
    private String notification; // 전체 공지

    @PrePersist // insert
    private void prePersist(){
        this.riterDay = LocalDate.now().toString();
    }
    @Builder
    public Party(Users user,String title,Boolean isOnline,String interest,String area,String content,int personnel,String notification){
        this.user= user;
        this.title = title;
        this.isOnline = isOnline;
        this.interest = interest;
        this.area = area;
        this.content = content;
        this.personnel = personnel;
        this.notification = notification;
    }
}
