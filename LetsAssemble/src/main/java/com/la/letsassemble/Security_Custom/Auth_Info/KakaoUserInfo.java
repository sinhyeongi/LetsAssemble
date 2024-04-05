package com.la.letsassemble.Security_Custom.Auth_Info;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private String id;
    private Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes, String id) {
        this.kakaoAccount = attributes;
        this.id = id;
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return null;
    }

//        @Override
//        public String getEmail() {
//            return String.valueOf(kakaoAccount.get("email"));
//        }

    @Override
    public String getName() {

        return String.valueOf(kakaoAccount.get("nickname"));
    }
}
