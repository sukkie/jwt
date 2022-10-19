package com.cos.jwt.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// FilterConfig로 대체
@Slf4j
public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        log.info("filter3 : " + req.getRequestURI());
//        if (req.getMethod().equals("POST")) {
            String headerAuth = req.getHeader("Authorization");
            log.info(headerAuth);

            if (headerAuth != null) {
                chain.doFilter(request, response);
            } else {
                PrintWriter pw = res.getWriter();
                pw.println("인증안됨");
            }
//        }
    }
}
