package com.la.letsassemble.Security_Custom;

import com.la.letsassemble.Entity.Users;
import com.la.letsassemble.Repository.UsersRepository;
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

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final UsersRepository repo;

    private final BCryptPasswordEncoder encoder;
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
        Optional<Users> userOptional =
                repo.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
        Users user;

        String provider = oAuth2UserInfo.getProvider(); // google , kakao, naver 등
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String password = encoder.encode("rfdgsvcgfdfb");
        String email = oAuth2UserInfo.getEmail();
        // 처음 서비스를 이용한 회원일 경우
        if(!userOptional.isPresent()){
            user = Users.builder()
                    .name(name)
                    .password(password)
                    .email(email)
                    .build();
            user.setProvider(provider);
            user.setProviderId(providerId);
        } else {
            user = userOptional.get();
        }


        return new PricipalDetails(user , oAuth2User.getAttributes());
    }
}
