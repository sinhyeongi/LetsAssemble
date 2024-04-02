package com.la.letsassemble.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id",referencedColumnName = "id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Party party_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "email",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;
    @CreationTimestamp
    @Column(name = "T_date",nullable = false)
    private String TDate;
    @Column(name = "content",nullable = false)
    private String content;

    @Builder
    public Message(Long party_id,String email,String content){
        this.party_id.setId(party_id);
        this.user.setEmail(email);
        this.content = content;
    }
}
