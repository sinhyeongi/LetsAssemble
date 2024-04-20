package com.la.letsassemble.Security_Custom.Auth_Info;



public interface OAuth2UserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();

}
