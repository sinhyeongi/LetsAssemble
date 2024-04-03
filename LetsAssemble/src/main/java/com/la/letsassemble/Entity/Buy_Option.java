package com.la.letsassemble.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class Buy_Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id",referencedColumnName = "id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Party party; // 파티 아이디
    @Column(nullable = false)
    private String even_day; // 옵션 실행 날짜
    @Column(nullable = false,name = "price")
    private int price; // 가격
    @Column(nullable = false)
    private String name; //옵션 이름
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplicant_id",referencedColumnName = "email",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user; // 파티장 이메일 / 아이디
    @Column(nullable = false,name = "imp_uid")
    private String impUid; // 결제 고유번호
    @Column(nullable = false)
    private String buy_day; // 구매 일
    @Column(columnDefinition = "TINYINT(1)",nullable = false)
    private boolean isOnline; // 온라인 여부

    @PrePersist
    private void prePersist(){
        this.buy_day = LocalDate.now().toString();
    }
    @Builder
    public Buy_Option(Long party_id, String even_day, int price, String name, String email, String imp_uid, boolean isOnline){
        this.party.setId(party_id);
        this.even_day = even_day;
        this.price = price;
        this.name =name;
        this.user.setEmail(email);
        this.impUid = imp_uid;
        this.isOnline = isOnline;
    }
}
