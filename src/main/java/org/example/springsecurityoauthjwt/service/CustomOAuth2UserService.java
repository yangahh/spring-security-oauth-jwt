package org.example.springsecurityoauthjwt.service;

import org.example.springsecurityoauthjwt.dto.*;
import org.example.springsecurityoauthjwt.entity.UserEntity;
import org.example.springsecurityoauthjwt.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) {
//            UserEntity userEntity = new UserEntity();
//            userEntity.setUsername(username);
//            userEntity.setName(oAuth2Response.getName());
//            userEntity.setEmail(oAuth2Response.getEmail());
//            userEntity.setRole("ROLE_USER");
//            userRepository.save(userEntity);
            UserEntity userEntity = createUser(username, oAuth2Response.getName(), oAuth2Response.getEmail(), "ROLE_USER");
            return toCustomOAuth2User(userEntity);
        } else { // 이미 회원가입이 되어있는 경우 -> 업데이트
//            existData.setEmail(oAuth2Response.getEmail());
//            existData.setName(oAuth2Response.getName());
//            userRepository.save(existData);
            UserEntity userEntity = updateUser(existData, oAuth2Response.getEmail(), oAuth2Response.getName());
            return toCustomOAuth2User(userEntity);
        }
    }

    private UserEntity createUser(String username, String name, String email, String role) {
        UserEntity userEntity = UserEntity.builder()
            .username(username)
            .name(name)
            .email(email)
            .role(role)
            .build();
        userRepository.save(userEntity);
        return userEntity;
    }

    private UserEntity updateUser(UserEntity userEntity, String email, String name) {
        userEntity.setEmail(email);
        userEntity.setName(name);
        userRepository.save(userEntity);
        return userEntity;
    }

    private CustomOAuth2User toCustomOAuth2User(UserEntity userEntity) {
        UserDTO user = UserDTO.builder()
            .username(userEntity.getUsername())
            .name(userEntity.getName())
            .email(userEntity.getEmail())
            .role(userEntity.getRole())
            .build();
        return new CustomOAuth2User(user);
    }
}
