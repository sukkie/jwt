package com.cos.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// FilterConfig로 대체
public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("filter3 : " + req.getRequestURI());
        if (req.getMethod().equals("POST")) {
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            if (headerAuth != null && headerAuth.equals("Bearer cos")) {
                chain.doFilter(request, response);
            } else {
                PrintWriter pw = res.getWriter();
                pw.println("인증안됨");
            }
        }
    }
}
