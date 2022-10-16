package com.cos.jwt.config.jwt;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서 username과 password를 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작함
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("--------------------");
        // 1. id, pw 받아서
        ObjectMapper om = new ObjectMapper();
        try {
            UserModel userModel = om.readValue(request.getInputStream(), UserModel.class);
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword());

            // PincipalDetailsService의 loadUserByUsername() 메서드가 실행
            Authentication authentication = getAuthenticationManager().authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUserModel());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. 정상인지 로그인 시도를 해본다. authenticationManager로 로그인 시도를 하면
        // PrincipalDetailService의 loadUserByUsername() 메소드가 호출되어 로그인 처리를 해준다.

        // 3. PrincipalDetails를 세션에 담고(권한괸리때문에 세션 사용

        // 4. JWT토큰을 만들어서 응답해주면 된다.
        return super.attemptAuthentication(request, response);
    }
}
