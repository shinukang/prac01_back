package com.example.demo.user.model;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

public class UserDto {
     @Getter
    @Builder
    public static class OAuth {
        private String email;
        private String name;
        private String provider;
        private boolean enable;
        private String role;

        public static OAuth from(Map<String, Object> attributes, String provider) {
            switch (provider) {
                case "kakao": return fromKakao(attributes);
                case "google": return fromGoogle(attributes);
                default: return null;
            }
        }

         private static OAuth fromKakao(Map<String, Object> attributes) {
             String providerId = ((Long) attributes.get("id")).toString();
             String email = providerId + "@kakao.social";
             Map properties = (Map) attributes.get("properties");
             String name = (String) properties.get("nickname");

             return OAuth.builder()
                     .email(email)
                     .name(name)
                     .provider("kakao")
                     .enable(true)
                     .role("ROLE_USER")
                     .build();
         }

         private static OAuth fromGoogle(Map<String, Object> attributes) {
             String providerId = (String) attributes.get("sub");
             String email = providerId + "@google.social";
             String name = (String) attributes.get("name");

             return OAuth.builder()
                     .email(email)
                     .name(name)
                     .provider("google")
                     .enable(true)
                     .role("ROLE_USER")
                     .build();
         }

         public User toEntity() {
            return User.builder()
                    .email(this.email)
                    .name(this.name)
                    .password(this.provider)
                    .enable(this.enable)
                    .role(this.role)
                    .build();
        }
    }

    @Getter
    public static class SignupReq {
        @Pattern(message = "이메일 형식이 아닙니다.", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        private String email;
        @Pattern(message = "이름은 한글로만 가능합니다.", regexp = "^[가-힣]{2,5}$")
        private String name;
        @Pattern(message = "비밀번호 양식이 틀렸습니다.", regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")
        private String password;

        public User toEntity() {
            return User.builder()
                    .email(this.email)
                    .name(this.name)
                    .password(this.password)
                    .enable(false)
                    .role("ROLE_USER")
                    .build();
        }
    }


    @Builder
    @Getter
    public static class SignupRes {
        private Long idx;
        private String email;
        private String name;

        public static SignupRes from(User entity) {
            return SignupRes.builder()
                    .idx(entity.getIdx())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .build();
        }
    }

    @Getter
    public static class LoginReq {
        private String email;
        private String password;
    }

    @Builder
    @Getter
    public static class LoginRes {
        private Long idx;
        private String email;
        private String name;

        public static LoginRes from(User entity) {
            return LoginRes.builder()
                    .idx(entity.getIdx())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .build();
        }
    }
}
