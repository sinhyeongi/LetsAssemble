package com.la.letsassemble.Security_Custom;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.UsersRepository;
import com.la.letsassemble.Role.UsersRole;
import com.la.letsassemble.Security_Custom.Auth_Info.GoogleUserInfo;
import com.la.letsassemble.Security_Custom.Auth_Info.KakaoUserInfo;
import com.la.letsassemble.Security_Custom.Auth_Info.NaverUserInfo;
import com.la.letsassemble.Security_Custom.Auth_Info.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final UsersRepository repo;

    private final BCryptPasswordEncoder encoder;
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"),
                    String.valueOf(oAuth2User.getAttributes().get("id")));
        }
        else{
            throw new OAuth2AuthenticationException("지원하지 않는 유저");
        }
        String email = oAuth2UserInfo.getEmail();
        String provider = oAuth2UserInfo.getProvider(); // google , kakao, naver 등
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String password = encoder.encode("rfdgsvcgfdfb");

        Users user = null;
        if(email != null){
            user = repo.findByEmail(email).orElse(null);
        }
        if(user == null){
            user = repo.findByProviderAndProviderId(provider,providerId).orElse(null);
        }

        if(user != null && user.getPhone() != null&& user.getProvider() == null){
            user.setProvider(provider);
            user.setProviderId(providerId);
            repo.save(user);
        } else if(user == null){
            user = Users.builder()
                    .name(name)
                    .password(password)
                    .build();
            if(email != null) {
                user.setEmail(email);
            }else{
                user.setEmail(provider+"_"+providerId);
            }
            user.setProvider(provider);
            user.setProviderId(providerId);
        }
        if(user.getRole() == null){
            user.setRole(UsersRole.ROLE_USER);
        }
        return new PricipalDetails(user);
    }
}
