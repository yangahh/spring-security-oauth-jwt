package org.example.springsecurityoauthjwt.oauth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springsecurityoauthjwt.dto.CustomOAuth2User;
import org.example.springsecurityoauthjwt.jwt.JWTUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // OAuth2User
        CustomOAuth2User oAuth2UserDetails = (CustomOAuth2User) authentication.getPrincipal();

        // JWT 토큰 생성: username, email, role 필요
        String username = oAuth2UserDetails.getUsername();

        String email = oAuth2UserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String jwtToken = jwtUtil.createJwt(username, email, role, 60 * 60 * 60L);// 1시간

        // 쿠키 방식으로 토큰 전달
        response.addCookie(createCookie("Authorization", jwtToken));
        // 프론트단으로 redirect
        response.sendRedirect("http://localhost:3000/");

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);  // only https
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
