package org.example.springsecurityoauthjwt.dto;

public interface OAuth2Response {
    // 제공자를 반환하는 메소드
    String getProvider();

    // 제공자 측에서 발급한 유저의 식별자
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 이름
    String getName();
}
