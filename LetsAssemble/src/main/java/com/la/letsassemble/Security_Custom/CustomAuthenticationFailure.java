package com.la.letsassemble.Security_Custom;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {
    public CustomAuthenticationFailure(){}

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String msg;
        if(exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException){
            msg = "아이디 또는 비밀번호가 틀립니다.";
        }else if(exception instanceof AccountExpiredException){
            msg = "계정이 만료 되었습니다.";
        }else if(exception instanceof CredentialsExpiredException){
            msg = "비밀번호가 만료 되었습니다. 비밀번호를 변경해 주세요";
        }else if(exception instanceof DisabledException){
            msg = "계정이 비활성화 된 상태입니다.";
        }else if(exception instanceof LockedException){
            msg = "계정이 정지된 상태입니다.";
        }else{
            msg = "알수없는 오류로인해 로그인 할 수없는 상태입니다.\n같은 증상이 반복 된다면 관리자에게 문의 해주세요";
        }
        msg = URLEncoder.encode(msg,"UTF-8");
        this.setDefaultFailureUrl("/user/ilmo_loginForm?error=true&exception="+msg);
        super.onAuthenticationFailure(request,response,exception);

    }
}
