package com.la.letsassemble.Entity;

import com.la.letsassemble.dto.PartyInfoForm;
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
    private Party party; // 파티 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplicant_id",referencedColumnName = "email")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user; // 신청자 / 유저 이메일
    private String state; //신청 상태 Y N W
    @Column(nullable = false)
    private String aplicant_day; //신청일
    @Column(nullable = false,columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean isBlack; //블랙 여부

    private String nickname; // 파티장이 정하는 별칭


    @PrePersist
    private void prepersiste(){
        this.aplicant_day = LocalDate.now().toString();
    }
    @Builder
    public PartyInfo(Party party,Users applicant_id,String state,Boolean isBlack){
        this.party = party;
        this.user = applicant_id;
        this.state = state;
        this.isBlack = isBlack;
    }

    public static PartyInfo updatePartyInfo(PartyInfo partyInfo, PartyInfoForm form){
        partyInfo.state = form.getState();
        partyInfo.isBlack = form.getIsBlack();
        return partyInfo;
    }

    public static PartyInfo updateNickname(PartyInfo partyInfo, PartyInfoForm form){
        partyInfo.nickname = form.getNickname();
        return partyInfo;
    }


}
