package com.la.letsassemble.Security_Custom.Auth_Info;

import java.util.Map;


public interface OAuth2UserInfo {

    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();

}
