package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login 요청해서 username과 password를 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작함
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 처리");
        // 1. id, pw 받아서
        ObjectMapper om = new ObjectMapper();
        try {
            UserModel userModel = om.readValue(request.getInputStream(), UserModel.class);
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword());

            // PincipalDetailsService의 loadUserByUsername() 메서드가 실행
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info(principalDetails.getUserModel().toString());

            // authentication객체가 세션에 저장
            // 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는것임.
            // 굳이 jwt토큰을 사용하면서 세션을 만들 이유가 없음. 단지 권한 처리때문에 세션에 넣어줌
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. 정상인지 로그인 시도를 해본다. authenticationManager로 로그인 시도를 하면
        // PrincipalDetailService의 loadUserByUsername() 메소드가 호출되어 로그인 처리를 해준다.

        // 3. PrincipalDetails를 세션에 담고(권한괸리때문에 세션 사용

        // 4. JWT토큰을 만들어서 응답해주면 된다.
        return null;
    }

    // attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication함수가 실행됨.
    // jwt토큰을 만들어서 리퀘스트 요청한 사용자에 jwt토큰을 응답해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("인증완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA방식이 아니고 Hash암호 방식
        String jwtToken = JWT.create()
                .withSubject("cos토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 10분
                .withClaim("id", principalDetails.getUserModel().getId())
                .withClaim("username", principalDetails.getUserModel().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
