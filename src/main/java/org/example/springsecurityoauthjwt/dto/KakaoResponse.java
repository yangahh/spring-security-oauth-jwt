package org.example.springsecurityoauthjwt.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final String id;
    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
//        {
//            id=3435221286,
//            connected_at=2024-04-13T10:32:55Z,
//            properties={nickname=hanah},
//            kakao_account={
//                profile_nickname_needs_agreement=false,
//                profile={
//                    nickname=hanah,
//                    is_default_nickname=false},
//                    has_email=true,
//                    email_needs_agreement=false,
//                    is_email_valid=true,
//                    is_email_verified=true,
//                    email=toaur6802@naver.com
//                }
//            }
//        }
        this.id = attribute.get("id").toString();
        this.attribute = (Map<String, Object>) attribute.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> profile = (Map<String, Object>) attribute.get("profile");
        return profile.get("nickname").toString();
    }
}
