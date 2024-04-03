package com.la.letsassemble.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Data
public class PartyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id",referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Party party_id; // 파티 번호
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplicant_id",referencedColumnName = "email")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user; // 신청자 / 유저 이메일
    private String state; //신청 상태
    @Column(nullable = false)
    private String aplicant_day; //신청일
    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean isBlack; //블랙 여부
    @PrePersist
    private void prepersiste(){
        this.aplicant_day = LocalDate.now().toString();
    }
    @Builder
    public PartyInfo(Long party_id,String applicant_id,String state,Boolean isBlack){
        this.party_id.setId(party_id);
        this.user.setEmail(applicant_id);
        this.state = state;
        this.isBlack = isBlack;
    }
}
