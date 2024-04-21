package com.la.letsassemble.Util;

import com.la.letsassemble.Entity.Buy_Option;
import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.Buy_OptionRepository;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Role.UsersRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InitLoadder implements CommandLineRunner {

    private final UsersRepository repo;
    private final PartyRepository partyRepository;
    private final Buy_OptionRepository buy_repo;
    private final BCryptPasswordEncoder encoder;
    private final PartyInfoRepository partyInfoRepository;
    public InitLoadder(UsersRepository repository, BCryptPasswordEncoder encoder, PartyRepository partyRepository, Buy_OptionRepository buyRepo,
                       PartyInfoRepository partyInfoRepository){
        repo = repository;
        this.encoder = encoder;
        this.partyRepository = partyRepository;
        this.buy_repo = buyRepo;
        this.partyInfoRepository = partyInfoRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Users admin = new Users().builder()
                .email("admin@LA.LA")
                .password(encoder.encode("admin"))
                .phone("010-1234-1234")
                .name("admin")
                .nickname("admin")
                .gender("M")
                .age(22)
                .build();
        repo.saveAndFlush(admin);
        admin.setRole(UsersRole.ROLE_ADMIN);
        repo.saveAndFlush(admin);
        Users u = new Users().builder()
                .email("test@test.tes")
                .password(encoder.encode("test"))
                .phone("010-1234-1234")
                .name("test")
                .nickname("test")
                .gender("M")
                .age(22)
                .build();
        repo.saveAndFlush(u);
        Users u2 = new Users().builder()
                .email("test2@test.tes")
                .password(encoder.encode("test2"))
                .phone("010-1234-1234")
                .name("test2")
                .nickname("test2")
                .gender("M")
                .age(33)
                .build();
        repo.saveAndFlush(u2);

        Users u3 = new Users().builder()
                .email("jkcoco123123@naver.com")
                .password(encoder.encode("test3"))
                .phone("010-1234-1234")
                .name("test3")
                .nickname("test3")
                .gender("M")
                .age(33)
                .build();
        repo.saveAndFlush(u3);

        Party party = new Party().builder()
                .isOnline(false)
                .personnel(100)
                .area("test")
                .content("test")
                .user(u)
                .interest("test")
                .notification("test")
                .title("test")
                .build();
        partyRepository.saveAndFlush(party);
        Party party2 = new Party().builder()
                .isOnline(false)
                .personnel(100)
                .area("test2")
                .content("test2")
                .user(u2)
                .interest("test2")
                .notification("test2")
                .title("test2")
                .build();
        partyRepository.saveAndFlush(party2);
        Party party3 = new Party().builder()
                .isOnline(false)
                .personnel(100)
                .area("test3")
                .content("test3")
                .user(u3)
                .interest("test3")
                .notification("test3")
                .title("test3")
                .build();
        Optional<Users> user = repo.findByEmail(u.getEmail());
        partyRepository.saveAndFlush(party3);

        for(int i = 0 ; i < 4; i++) {
            Buy_Option option = new Buy_Option().builder()
                    .party(party)
                    .even_day("2024-04-22")
                    .price(1000)
                    .name("test")
                    .user(user.get())
                    .imp_uid("test")
                    .isOnline(party.isOnline())
                    .build();
            buy_repo.save(option);
        }
        for(int i = 0 ; i < 10; i++) {
            Buy_Option option = new Buy_Option().builder()
                    .party(party)
                    .even_day("2024-05-23")
                    .price(1000)
                    .name("test")
                    .user(user.get())
                    .imp_uid("test")
                    .isOnline(party.isOnline())
                    .build();
            buy_repo.save(option);
        }
        Buy_Option option = new Buy_Option().builder()
                .party(party)
                .even_day("2024-04-19")
                .price(1000)
                .name("test")
                .user(user.get())
                .imp_uid("test")
                .isOnline(party.isOnline())
                .build();
        buy_repo.save(option);
        PartyInfo partyInfo1_1 = PartyInfo.builder()
                .party(party)
                .applicant_id(user.get())
                .state("Y")
                .isBlack(false)
                .build();
        PartyInfo partyInfo1_2 = PartyInfo.builder()
                .party(party)
                .applicant_id(u2)
                .state("Y")
                .isBlack(false)
                .build();
        PartyInfo partyInfo1_3 = PartyInfo.builder()
                .party(party)
                .applicant_id(u3)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo2_1 = PartyInfo.builder()
                .party(party2)
                .applicant_id(user.get())
                .state("Y")
                .isBlack(false)
                .build();
        PartyInfo partyInfo2_2 = PartyInfo.builder()
                .party(party2)
                .applicant_id(u2)
                .state("Y")
                .isBlack(false)
                .build();
        PartyInfo partyInfo2_3 = PartyInfo.builder()
                .party(party2)
                .applicant_id(u3)
                .state("W")
                .isBlack(false)
                .build();
        PartyInfo partyInfo3_1 = PartyInfo.builder()
                .party(party3)
                .applicant_id(user.get())
                .state("Y")
                .isBlack(false)
                .build();
        PartyInfo partyInfo3_2 = PartyInfo.builder()
                .party(party3)
                .applicant_id(u2)
                .state("W")
                .isBlack(false)
                .build();
        PartyInfo partyInfo3_3 = PartyInfo.builder()
                .party(party3)
                .applicant_id(u3)
                .state("Y")
                .isBlack(false)
                .build();
        partyInfoRepository.save(partyInfo1_1);
        partyInfoRepository.save(partyInfo1_2);
        partyInfoRepository.save(partyInfo1_3);
        partyInfoRepository.save(partyInfo2_1);
        partyInfoRepository.save(partyInfo2_2);
        partyInfoRepository.save(partyInfo2_3);
        partyInfoRepository.save(partyInfo3_1);
        partyInfoRepository.save(partyInfo3_2);
        partyInfoRepository.save(partyInfo3_3);
        }
}
