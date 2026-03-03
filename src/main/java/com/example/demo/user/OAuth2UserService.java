package com.example.demo.user;

import com.example.demo.user.model.AuthUserDetails;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("서비스 코드 실행");
        // provider 구분
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 0. OAuth2 로그인 실행
        OAuth2User oAuth2User = super.loadUser(userRequest);

        UserDto.OAuth dto = UserDto.OAuth.from(oAuth2User.getAttributes(), provider);

        // 1. DB에서 회원 확인
        Optional<User> user =  userRepository.findByEmail(dto.getEmail());
        // 2. 없다면 회원 가입(저장)
        if (user.isEmpty()) {
            return AuthUserDetails.from(dto.toEntity());
        }
        // 3. 있다면 해당 사용자 반환
        else {
            return AuthUserDetails.from(user.get());
        }
    }
}
