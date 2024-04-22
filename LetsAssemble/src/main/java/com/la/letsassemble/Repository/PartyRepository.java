package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.QueryDsl.PartyCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PartyRepository extends JpaRepository<Party,Long>, PartyCustomRepository {
    List<Party> findAllByUser(Users user);
    @Query("SELECT pi.party FROM PartyInfo pi WHERE pi.user = :user AND pi.state = :state AND pi.party.user.email != pi.user.email")
    List<Party> findAllByUserAndState(Users user,String state);

    // 온라인인 무료 파티 전체 가져오기
    @Query("SELECT p FROM Party p LEFT JOIN Buy_Option bo ON p.id = bo.party.id " +
            "WHERE (bo.party IS NULL OR p.id <> bo.party.id) AND p.isOnline = true " +
            "ORDER BY p.id ASC")
    List<Party> findOnlineParties();


    // 오프라인 무료 파티 전체 가져오기
    @Query("SELECT p FROM Party p LEFT JOIN Buy_Option bo ON p.id = bo.party.id " +
            "WHERE (bo.party IS NULL OR p.id <> bo.party.id) AND p.isOnline = false " +
            "ORDER BY p.id ASC")
    List<Party> findOfflineParties();


    // 무료인 전체 리스트
    @Query("SELECT p FROM Party p LEFT JOIN Buy_Option bo ON p.id = bo.party.id " +
            "WHERE (bo.party IS NULL OR p.id <> bo.party.id)" +
            "ORDER BY p.id ASC")
    List<Party> findAllList();

    // 유료 전체 리스트 - 구분 없음
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id " +
            "              AND bo.even_day = :currentDate) " +  // buy_option의 even_day가 현재 날짜와 같은 경우
            "ORDER BY p.id ASC")  // buy_option의 id를 내림차순으로 정렬
    List<Party> findAllMoneyAllList(String currentDate);

    // 유료 전체 리스트들 isOnlie의 여부에 따라 달라짐
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id " +
            "              AND bo.even_day = :currentDate " +  // buy_option의 even_day가 현재 날짜와 같은 경우
            "              AND bo.party.isOnline = :isOnline) " +
            "ORDER BY p.id ASC")  // buy_option의 id를 내림차순으로 정렬
    List<Party> findAllMoneyDivisionList(String currentDate , Boolean isOnline); // 구분에 따른 유료 리스트


    // 유료 무료 상관 없는 모든 파티리스트
    // 모든 파티 정보를 가져오는 메서드
    List<Party> findAll();
}
