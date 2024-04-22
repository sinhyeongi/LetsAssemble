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

import java.util.Arrays;
import java.util.List;
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

        Users u5 = new Users().builder()
                .email("test5@test.tes")
                .password(encoder.encode("test5"))
                .phone("010-1234-1234")
                .name("김두한")
                .nickname("종로원펀치")
                .gender("M")
                .age(27)
                .build();
        repo.saveAndFlush(u5);

        Users u6 = new Users().builder()
                .email("test6@test.tes")
                .password(encoder.encode("test6"))
                .phone("010-1234-1234")
                .name("홍해인")
                .nickname("눈물의여왕")
                .gender("F")
                .age(27)
                .build();
        repo.saveAndFlush(u6);

        Users u7 = new Users().builder()
                .email("test7@test.tes")
                .password(encoder.encode("test7"))
                .phone("010-1234-1234")
                .name("곽철용")
                .nickname("묻고더블로가")
                .gender("M")
                .age(53)
                .build();
        repo.saveAndFlush(u7);

        Users u8 = new Users().builder()
                .email("test8@test.tes")
                .password(encoder.encode("test8"))
                .phone("010-1234-1234")
                .name("김광규")
                .nickname("느그아버지")
                .gender("M")
                .age(60)
                .build();
        repo.saveAndFlush(u8);

        Users u9 = new Users().builder()
                .email("test9@test.tes")
                .password(encoder.encode("test9"))
                .phone("010-1234-1234")
                .name("진도준")
                .nickname("분당땅부자")
                .gender("M")
                .age(22)
                .build();
        repo.saveAndFlush(u9);

        Users u10 = new Users().builder()
                .email("test10@test.tes")
                .password(encoder.encode("test10"))
                .phone("010-1234-1234")
                .name("진양철")
                .nickname("순양자동차")
                .gender("M")
                .age(88)
                .build();
        repo.saveAndFlush(u10);

        Users u11 = new Users().builder()
                .email("test11@test.tes")
                .password(encoder.encode("test11"))
                .phone("010-1234-1234")
                .name("차무식")
                .nickname("카지노ㄱㄱ")
                .gender("M")
                .age(44)
                .build();
        repo.saveAndFlush(u11);

        Users u12 = new Users().builder()
                .email("test12@test.tes")
                .password(encoder.encode("test12"))
                .phone("010-1234-1234")
                .name("손석구")
                .nickname("너납치됨")
                .gender("M")
                .age(31)
                .build();
        repo.saveAndFlush(u12);

        // 추가.  나중에 관리자 -> 회원 삭제 시연할때 쓸 더미임
        Users u4 = new Users().builder()
                .email("tlsrlgns9805@naver.com")
                .password(encoder.encode("1234"))
                .phone("010-9999-7777")
                .name("신기훈")
                .nickname("감자도리")
                .gender("M")
                .age(28)
                .build();
        repo.saveAndFlush(u4);

        Party party = new Party().builder()
                .isOnline(false)
                .personnel(10)
                .area("경기")
                .content("차 있으신 분들 대환영입니다")
                .user(u)
                .interest("travel")
                .notification("이상한 짓 하시면 경찰에 신고할게요")
                .title("서울, 경기 근교로 같이 여행 가실분 들어와주세요")
                .build();
        partyRepository.saveAndFlush(party);
        Party party2 = new Party().builder()
                .isOnline(false)
                .personnel(25)
                .area("서울")
                .content("실력 상관없이 즐겁게 하실분들 모두 환영해요")
                .user(u2)
                .interest("sports")
                .notification("축구화는 필수입니다 !!!")
                .title("축구하고 싶은데 못 하셨던 분들 ! 같이 하시려면 들어오세요")
                .build();
        partyRepository.saveAndFlush(party2);
        Party party3 = new Party().builder()
                .isOnline(false)
                .personnel(8)
                .area("경기")
                .content("남녀노소 누구나 환영합니다 ㅎㅎ")
                .user(u3)
                .interest("boardGame")
                .notification("서로 사이좋게 했으면 좋겠어요")
                .title("보드게임 카페가서 재밌게 보드게임 하실 분들 구해요 !!")
                .build();
        partyRepository.saveAndFlush(party3);

        // 유저 객체 추가
        Optional<Users> user = repo.findByEmail(u.getEmail());
        Optional<Users> user2 = repo.findByEmail(u2.getEmail());
        Optional<Users> user3 = repo.findByEmail(u3.getEmail());
        Optional<Users> user4 = repo.findByEmail(u4.getEmail());



        // 테스트용 더미 추가
        Party party4 = new Party().builder()
                .isOnline(true)
                .personnel(50)
                .area("전남")
                .content("성실하게 참여하실 분들 많이 환영합니다 ~~ !")
                .user(u2)
                .interest("game")
                .notification("비매너 유저는 추방할게요 ! ")
                .title("같이 서든어택 하실분 일단모여 !!!")
                .build();
        partyRepository.saveAndFlush(party4);

        Party party5 = new Party().builder()
                .isOnline(true)
                .personnel(10)
                .area("경북")
                .content("티어 상관없이 즐겁게 하실 분들만 신청해주세요")
                .user(u3)
                .interest("game")
                .notification("비매너 유저는 추방할게요 ! ")
                .title("같이 롤하실 분들 4명 구합니다!")
                .build();
        partyRepository.saveAndFlush(party5);

        Party party6 = new Party().builder()
                .isOnline(true)
                .personnel(10)
                .area("인천")
                .content("고수들만 들어와주세요 !!!")
                .user(u)
                .interest("game")
                .notification("싸우면 추방합니다")
                .title("오버워치 하실분들 저희 파티 들어와서 같이 해요 ~~ ")
                .build();
        partyRepository.saveAndFlush(party6);

        Party party7 = new Party().builder()
                .isOnline(false)
                .personnel(50)
                .area("서울")
                .content("열정이 많으신 분들만 신청 부탁드릴게요")
                .user(u3)
                .interest("study")
                .notification("서로 도와주며 한달안에 프로젝트 완성하는 것이 목표입니다")
                .title("웹 개발 프로젝트 같이 만드실 분들 눌러주세요 ! ")
                .build();
        partyRepository.saveAndFlush(party7);

        Party party8 = new Party().builder()
                .isOnline(false)
                .personnel(22)
                .area("경기")
                .content("실력이 낮아도 괜찮으니 즐겁게 하시고 싶은 분들 환영해요 ~")
                .user(u2)
                .interest("sports")
                .notification("야구 장비는 꼭!! 필수입니다 ")
                .title("야구하러 가실분 들어와주세요")
                .build();
        partyRepository.saveAndFlush(party8);

        Party party9 = new Party().builder()
                .isOnline(false)
                .personnel(50)
                .area("강원")
                .content("서로 봐주면서 피드백 및 첨삭하는 방식입니다")
                .user(u)
                .interest("study")
                .notification("편한 분위기로 다 같이 열심히 했으면 좋겠습니다")
                .title("면접 준비하시는 취준생 분들 같이 면접 준비해요 !!")
                .build();
        partyRepository.saveAndFlush(party9);

        Party party10 = new Party().builder()
                .isOnline(true)
                .personnel(50)
                .area("제주")
                .content("주 2회 스터디 예정입니다 ! 많이 참여 부탁드려요")
                .user(u2)
                .interest("study")
                .notification("약속 시간 안 지키시면 추방할게요")
                .title("화상 채팅으로 개발 스터디 하실 분들 구해요!")
                .build();
        partyRepository.saveAndFlush(party10);

        Party party11 = new Party().builder()
                .isOnline(false)
                .personnel(50)
                .area("경남")
                .content("차 없으셔도 됩니다! 여행에 필요한 비용만 있으면 돼요")
                .user(u3)
                .interest("travel")
                .notification("시간 약속은 필수로 지켜주셔야 합니다")
                .title("해운대 가실 성격 좋으신 분들 구해요 ~ ")
                .build();
        partyRepository.saveAndFlush(party11);

        Party party12 = new Party().builder()
                .isOnline(false)
                .personnel(50)
                .area("서울")
                .content("열정이 많으신 분들만 신청 부탁드릴게요")
                .user(u3)
                .interest("study")
                .notification("서로 도와주며 한달안에 프로젝트 완성하는 것이 목표입니다")
                .title("웹 개발 프로젝트 같이 만드실 분들 눌러주세요 ! ")
                .build();
        partyRepository.saveAndFlush(party12);

        Party party13 = new Party().builder()
                .isOnline(true)
                .personnel(10)
                .area("서울")
                .content("고수들만 들어와주세요 !!!")
                .user(u)
                .interest("game")
                .notification("싸우면 추방합니다")
                .title("온라인 게임 같이 하실분들 저희 파티 들어와서 같이 해요 ~~ ")
                .build();
        partyRepository.saveAndFlush(party13);

        Party party14 = new Party().builder()
                .isOnline(false)
                .personnel(22)
                .area("서울")
                .content("실력이 낮아도 괜찮으니 즐겁게 하시고 싶은 분들 환영해요 ~")
                .user(u2)
                .interest("sports")
                .notification("야구 장비는 꼭!! 필수입니다 ")
                .title("야구하러 가실분 들어와주세요")
                .build();
        partyRepository.saveAndFlush(party14);
        // 더미 끝
        for(int i = 0 ; i < 10; i++) {
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
        for(int i = 0 ; i < 8; i++) {
            Buy_Option option = new Buy_Option().builder()
                    .party(party)
                    .even_day("2024-04-24")
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
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user.get())
                .imp_uid("test")
                .isOnline(party.isOnline())
                .build();
        buy_repo.save(option);

       // 유료 테스트 더미

        Buy_Option option2 = new Buy_Option().builder()
                .party(party2)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user.get())
                .imp_uid("test")
                .isOnline(false)
                .build();
        buy_repo.save(option2);

        Buy_Option option3 = new Buy_Option().builder()
                .party(party3)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user.get())
                .imp_uid("test")
                .isOnline(false)
                .build();
        buy_repo.save(option3);

        Buy_Option option4 = new Buy_Option().builder()
                .party(party4)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user2.get())
                .imp_uid("test")
                .isOnline(true)
                .build();
        buy_repo.save(option4);

        Buy_Option option5 = new Buy_Option().builder()
                .party(party5)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user3.get())
                .imp_uid("test")
                .isOnline(true)
                .build();
        buy_repo.save(option5);

        Buy_Option option6 = new Buy_Option().builder()
                .party(party6)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user.get())
                .imp_uid("test")
                .isOnline(true)
                .build();
        buy_repo.save(option6);

        Buy_Option option7 = new Buy_Option().builder()
                .party(party7)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user3.get())
                .imp_uid("test")
                .isOnline(false)
                .build();
        buy_repo.save(option7);

        Buy_Option option8 = new Buy_Option().builder()
                .party(party7)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user2.get())
                .imp_uid("test")
                .isOnline(false)
                .build();
        buy_repo.save(option8);

        Buy_Option option11 = new Buy_Option().builder()
                .party(party11)
                .even_day("2024-04-22")
                .price(1000)
                .name("test")
                .user(user3.get())
                .imp_uid("test")
                .isOnline(false)
                .build();
        buy_repo.save(option11);
        // 유료 더미 끝


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

        // 파티 인포 더미 추가
        PartyInfo partyInfo4 = PartyInfo.builder()
                .party(party4)
                .applicant_id(u2)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo5 = PartyInfo.builder()
                .party(party5)
                .applicant_id(u3)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo6 = PartyInfo.builder()
                .party(party6)
                .applicant_id(u)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo7 = PartyInfo.builder()
                .party(party7)
                .applicant_id(u3)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo8 = PartyInfo.builder()
                .party(party8)
                .applicant_id(u2)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo9 = PartyInfo.builder()
                .party(party9)
                .applicant_id(u)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo10 = PartyInfo.builder()
                .party(party10)
                .applicant_id(u2)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo11 = PartyInfo.builder()
                .party(party11)
                .applicant_id(u3)
                .state("Y")
                .isBlack(false)
                .build();
    List<Party> parties = Arrays.asList(party, party2, party3, party4, party5, party6, party7, party8, party9, party10,party11,party12,party13,party14);
    List<Users> applicants = Arrays.asList(u5, u6, u7, u8, u9, u10, u11, u12);

        for (Party currentParty : parties) {
            for (Users currentApplicant : applicants) {
                PartyInfo partyInfo = PartyInfo.builder()
                        .party(currentParty)
                        .applicant_id(currentApplicant)
                        .state("Y")  // Set state and isBlack according to your requirements
                        .isBlack(false)
                        .build();
                partyInfoRepository.save(partyInfo);
            }
        }

        PartyInfo partyInfo12 = PartyInfo.builder()
                .party(party12)
                .applicant_id(u3)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo13 = PartyInfo.builder()
                .party(party13)
                .applicant_id(u)
                .state("Y")
                .isBlack(false)
                .build();

        PartyInfo partyInfo14 = PartyInfo.builder()
                .party(party14)
                .applicant_id(u2)
                .state("Y")
                .isBlack(false)
                .build();

        // 인포 더미 추가 끝
        partyInfoRepository.save(partyInfo1_1);
        partyInfoRepository.save(partyInfo1_2);
        partyInfoRepository.save(partyInfo1_3);
        partyInfoRepository.save(partyInfo2_1);
        partyInfoRepository.save(partyInfo2_2);
        partyInfoRepository.save(partyInfo2_3);
        partyInfoRepository.save(partyInfo3_1);
        partyInfoRepository.save(partyInfo3_2);
        partyInfoRepository.save(partyInfo3_3);

        // 추가
        partyInfoRepository.save(partyInfo4);
        partyInfoRepository.save(partyInfo5);
        partyInfoRepository.save(partyInfo6);
        partyInfoRepository.save(partyInfo7);
        partyInfoRepository.save(partyInfo8);
        partyInfoRepository.save(partyInfo9);
        partyInfoRepository.save(partyInfo10);
        partyInfoRepository.save(partyInfo11);
        partyInfoRepository.save(partyInfo12);
        partyInfoRepository.save(partyInfo13);
        partyInfoRepository.save(partyInfo14);
        }
}
