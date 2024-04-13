package org.example.springsecurityoauthjwt.service;

import org.example.springsecurityoauthjwt.dto.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 어느 서비스에서 유저 정보를 가지고 왔는지
        OAuth2Response oAuth2Response = null;

        if (registrationId.equalsIgnoreCase("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equalsIgnoreCase("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equalsIgnoreCase("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider()+" "+ oAuth2Response.getProviderId();

        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setName(oAuth2Response.getName());
        user.setEmail(oAuth2Response.getEmail());
        user.setRole("ROLE_USER");
        return new CustomOAuth2User(user);
    }
}
