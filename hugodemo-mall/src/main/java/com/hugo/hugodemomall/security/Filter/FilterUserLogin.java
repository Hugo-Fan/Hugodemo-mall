package com.hugo.hugodemomall.security.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
@Slf4j
public class FilterUserLogin extends OncePerRequestFilter {
    // 紀錄登入時間
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if(url.equals("/members/login")){
            String userAgent = request.getHeader("User-Agent");
            String host = request.getHeader("Host");
            Date now = new Date();

            log.info("使用者 {} 於 " + now + " 嘗試從 " + host +" 登入",userAgent);
        }
        // request 和 response 繼續往下傳遞
        filterChain.doFilter(request,response);
    }
}
