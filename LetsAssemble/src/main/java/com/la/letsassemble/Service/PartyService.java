package com.la.letsassemble.Service;

import com.la.letsassemble.Entity.Party;
import com.la.letsassemble.Entity.PartyInfo;
import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.PartyInfoRepository;
import com.la.letsassemble.Repository.PartyRepository;
import com.la.letsassemble.Repository.RedisLockRepository;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.dto.PartyForm;
import com.la.letsassemble.dto.PartyInfoForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {
    private final PartyRepository repo;
    private final UsersRepository usersRepository;

    private final PartyInfoRepository partyInfoRepository;
    private final RedisLockRepository redisLockRepository;

    public Optional<Party> findByPartyId(Long id){
        return repo.findById(id);
    }

    public List<String> getArea(){
        return repo.GetArea();
    }
    @Transactional
    public String delegateParty(Long partyId,Users user,Long userId){
        try {
            int count = 0;
            while(count < 10){
                if(redisLockRepository.lock("party","delegate")){
                    Optional<Party> optionalParty = repo.findById(partyId);
                    if(!optionalParty.isPresent())return "no party";

                    Party party = optionalParty.get();
                    if(!party.getUser().getEmail().equals(user.getEmail()))return "no host";

                    Optional<Users> optionalUsers = usersRepository.findById(userId);
                    if(!optionalUsers.isPresent())return "no user";

                    Users recevingUser = optionalUsers.get();
                    party = Party.delegateParty(party, recevingUser);
                    repo.saveAndFlush(party);
                    return recevingUser.getNickname();
                }else{
                    Thread.sleep(1000);
                    count++;
                    if(count >= 10){
                        throw new Exception();
                    }
                }
            }
        }catch (Exception e){
            return "error";
        }finally {
            redisLockRepository.unlock("party","delegate");
        }
        return "fail";
    }
    @Transactional
    public String deleteParty(Long id,Users user,String inputPartyName){
        try {
            int count = 0;
            while(count < 10){
                if(redisLockRepository.lock("party","delete")){
                    Optional<Party> optionalParty = repo.findById(id);
                    if(!optionalParty.isPresent())return "no party";
                    Party party = optionalParty.get();
                    if(!party.getUser().getEmail().equals(user.getEmail()))return "no host";
                    if(!party.getTitle().equals(inputPartyName))return "no title";

                    repo.deleteById(id);
                    return "ok";
                }else{
                    Thread.sleep(1000);
                    count ++;
                    if(count >= 10){
                        throw new Exception();
                    }
                }
            }
        }catch (Exception e){

        }finally {
            redisLockRepository.unlock("party","delete");
        }
        return "fail";
    }

    @Transactional
    public String createParty(PartyForm from, Users user){
        try {

            if (user.getPhone() == null) {
                return "no phone";
            }
            //관심사가 없을 시
            if (from.getCategory() == null) {
                return "no category";
            }
            //온라인 여부 없을 시
            if (from.getIsOnline() == null) {
                return "no isOnline";
            }
            //모집인원 없을 시
            if (from.getCapacity() == null) {
                return "no capacity";
            }
            //파티이름 없을 시
            if (from.getName() == null || from.getName().trim().length() < 2) {
                return "no name";
            }
            //주소 입력 없을 시
            if (from.getAddress().isBlank()) {
                return "no address";
            }
            int count =0;
            while(!redisLockRepository.lock("party_create","party_create") && count < 10){
                Thread.sleep(1000);
                count++;
            }
            if(count >= 10){
                throw new Exception();
            }
            if(from.getAddress().indexOf(" ") != -1){
                from.setAddress(from.getAddress().substring(0,2));
            }
            //파티 생성
            Party party = Party.builder()
                    .interest(from.getCategory()) //관심사
                    .title(from.getName()) //파티이름
                    .user(user)// 파티장
                    .isOnline(from.getIsOnline().equals("online"))//온라인 여부
                    .area(from.getAddress())
                    .personnel(Integer.parseInt(from.getCapacity()))
                    .build();
            System.out.println("party = " + party);
            repo.save(party);
            //파티정보 생성
            PartyInfo partyInfo = PartyInfo.builder()
                    .party(party)
                    .applicant_id(user)
                    .state("Y")
                    .isBlack(false)
                    .build();

            partyInfoRepository.saveAndFlush(partyInfo);
            return party.getId().toString();
        }catch (Exception e){

        }finally {
            redisLockRepository.unlock("party","create");
        }
        return null;
    }
    @Transactional
    public void updateParty(PartyForm partyForm){
        try {
            int count = 0;
            while(count < 10) {
                if(redisLockRepository.lock("party","update")){
                    Optional<Party> party = repo.findById(Long.parseLong(partyForm.getPartyId()));
                    if (party.isPresent()) {
                        Party updateparty = Party.updateParty(party.get(), partyForm);
                        repo.saveAndFlush(updateparty);
                    }
                    return;
                }else{
                    Thread.sleep(1000);
                    count++;
                    if(count >= 10){
                        throw new Exception();
                    }
                }
            }
        }catch (Exception e) {

        }finally {
            redisLockRepository.unlock("party","update");
        }
    }
    public List<Party> findAllByUser(Users user) {
        return repo.findAllByUser(user);
    }
    public List<Party> findAllByUserAndState(Users user,String state){
        return repo.findAllByUserAndState(user,state);
    }

    // 온라인 무료 리스트
    public List<Party> findOnlieList(){
        return repo.findOnlineParties();
    }

    // 온라인 무료 리스트
    public List<Party> findOfflieList(){
        return repo.findOfflineParties();
    }

    // 무료 전체 리스트
    public List<Party> findAllList(){
        return repo.findAllList();
    }

    // 유료 전체 리스트 4개
    public List<Party> findFourMoneyAllList(int limit){
        return repo.findFourMoneyAllList(limit);
    }
    public List<Long> findUserCounter(List<Party> list){
        return partyInfoRepository.findUserCounter_Party(list);
    }
    // 유료 리스트 4개
    public List<Party> findFourMoneyOnlieList(int limit){
        return repo.findFourMoneyOnlineList(limit);
    }
    // 무료 리스트 4개
    public List<Party> findFourMoneyOffLineList(int limit){
        return repo.findFourMoneyOfflineList(limit);
    }

    // 지역들
    public List<Party> findAllParty(){
        return repo.findAll();
    }

    @Transactional
    public String applyJoinParty(Party party, Users user, PartyInfoForm form){
        try {
            int count = 0;
            while( count < 10) {
                if (redisLockRepository.lock("party", "join")) {
                    if (party.getUser().getEmail().equals(user.getEmail())) {
                        //파티장인경우
                        return "host";
                    }
                    //기존 파티 정보가 있을 경우
                    Optional<PartyInfo> optionalPartyInfo = partyInfoRepository.findByParty_IdAndUserEmail(party.getId(), user.getEmail());
                    if (optionalPartyInfo.isPresent()) {
                        PartyInfo partyInfo = optionalPartyInfo.get();
                        //가입 거절 상태
                        if (partyInfo.isBlack()) {
                            return "black";
                        }
                        //기존 정보 업데이트
                        partyInfo = PartyInfo.updatePartyInfo(partyInfo, form);
                        partyInfoRepository.saveAndFlush(partyInfo);
                        return "success";
                    }
                    //이외 가입 신청 하기
                    PartyInfo partyInfo = PartyInfo.builder()
                            .party(party)
                            .applicant_id(user)
                            .state("W")
                            .isBlack(false)
                            .build();
                    partyInfoRepository.saveAndFlush(partyInfo);
                    return "success";
                } else {
                    Thread.sleep(1000);
                    count++;
                    if (count >= 10) {
                        throw new Exception();
                    }
                }
            }
        }catch (Exception e){

        }finally {
            redisLockRepository.unlock("party","join");
        }
        return "Fail";
    }

    @Transactional
    public String deleteUser(Long id) {
        repo.deleteById(id);
        return "ok";
    }
}


