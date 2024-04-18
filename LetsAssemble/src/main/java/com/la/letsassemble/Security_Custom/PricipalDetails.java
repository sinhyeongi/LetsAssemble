package com.la.letsassemble.Security_Custom;

import com.la.letsassemble.Entity.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PricipalDetails implements UserDetails, OAuth2User {
    private Users user;
    private Map<String,Object> attr;

    public PricipalDetails(Users user){
        this.user = user;

    }
    public PricipalDetails(Users user, Map<String, Object> attr) {
        this.user = user;
        this.attr = attr;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return attr;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //계정 잠김 여부
    public boolean isAccountNonLocked() {
        if(user.getSuspensionPeriod()==null){
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate udate = LocalDate.parse(user.getSuspensionPeriod());
        return today.isBefore(udate);
    }

    @Override // 비밀번호 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //계정 활성화 여부
    public boolean isEnabled() {

        return true;
    }
    
    public String getEmail(){
        return user.getEmail();
    }

}
