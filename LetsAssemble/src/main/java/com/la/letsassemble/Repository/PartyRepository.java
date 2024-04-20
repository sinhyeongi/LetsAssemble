package com.la.letsassemble.Repository;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party,Long> {
    List<Party> findAllByUser(Users user);
    @Query("SELECT pi.party FROM PartyInfo pi WHERE pi.user = :user AND pi.state = :state AND pi.party.user.email != pi.user.email")
    List<Party> findAllByUserAndState(Users user,String state);

    // 온라인인 무료 파티 전체 가져오기
    @Query("SELECT p FROM Party p LEFT JOIN Buy_Option bo ON p.id = bo.party.id " +
            "WHERE (bo.party IS NULL OR p.id <> bo.party.id) AND p.isOnline = true " +
            "ORDER BY p.id DESC")
    List<Party> findOnlineParties();


    // 오프라인 무료 파티 전체 가져오기
    @Query("SELECT p FROM Party p LEFT JOIN Buy_Option bo ON p.id = bo.party.id " +
            "WHERE (bo.party IS NULL OR p.id <> bo.party.id) AND p.isOnline = false " +
            "ORDER BY p.id DESC")
    List<Party> findOfflineParties();


    // 무료인 전체 리스트
    @Query("SELECT p FROM Party p LEFT JOIN Buy_Option bo ON p.id = bo.party.id " +
            "WHERE (bo.party IS NULL OR p.id <> bo.party.id)" +
            "ORDER BY p.id DESC")
    List<Party> findAllList();

    // 유료 전체 리스트
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id ) " +
            "ORDER BY p.riterDay DESC")
    List<Party> findAllMoneyAllList();

    // 유료 전체 중 4개 내림차순
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id ) " +
            "ORDER BY p.riterDay DESC")
    List<Party> findFourMoneyAllList(int limit);

    // 유료 온라인 전체 (내림차순)
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id AND bo.party.isOnline = true) " +
            "ORDER BY p.riterDay DESC")
    List<Party> findAllMoneyOnlineList();

    // 유료 온라인 4개
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id AND bo.party.isOnline = true) " +
            "ORDER BY p.riterDay DESC")
    List<Party> findFourMoneyOnlineList(int limit);

    // 유료 오프라인 전체 (내림차순)
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id AND bo.party.isOnline = false) " +
            "ORDER BY p.riterDay DESC")
    List<Party> findAllMoneyOfflineList();

    // 유료 오프라인 4개
    @Query("SELECT p FROM Party p " +
            "WHERE p.id IN (SELECT bo.party.id FROM Buy_Option bo WHERE bo.party.id = p.id AND bo.party.isOnline = false) " +
            "ORDER BY p.riterDay DESC")
    List<Party> findFourMoneyOfflineList(int limit);

    // 유료 무료 상관 없는 모든 파티리스트
    // 모든 파티 정보를 가져오는 메서드
    List<Party> findAll();
}
