package mykeywordnews.mykeynews.service;

import lombok.RequiredArgsConstructor;
import mykeywordnews.mykeynews.common.jwt.TokenProvider;
import mykeywordnews.mykeynews.domain.*;
//import mykeywordnews.mykeynews.dto.KeywordRequestDto;
import mykeywordnews.mykeynews.dto.SigninRequestDto;
import mykeywordnews.mykeynews.dto.TokenDto;
import mykeywordnews.mykeynews.dto.UserRequestDto;
import mykeywordnews.mykeynews.dto.UserResponseDto;
import mykeywordnews.mykeynews.repository.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeywordRepository keywordRepository;
    private final TokenProvider tokenProvider;
    private final ZumKeywordRepository zumKeywordRepository;
    private final BeingKeywordRepository beingKeywordRepository;
    private final NateKeywordRepository nateKeywordRepository;

    //회원가입을 하는 메소드
    public UserResponseDto signup(UserRequestDto requestDto) {
        if (userRepository.existsByUserId(requestDto.getUserId())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        User user = requestDto.toUser(passwordEncoder);

        List<String> userKeyword = requestDto.getUserKeyword();
        for (String s : userKeyword) {
            System.out.println("s = " + s);
            NaverKeyword keyword = new NaverKeyword(s);
            ZumKeyword zumKeyword = new ZumKeyword(s);
            BeingKeyword beingKeyword = new BeingKeyword(s);
            NateKeyword nateKeyword = new NateKeyword(s);
            keyword.setUser(user);
            zumKeyword.setUser(user);
            beingKeyword.setUser(user);
            nateKeyword.setUser(user);
            keywordRepository.save(keyword);
            zumKeywordRepository.save(zumKeyword);
            beingKeywordRepository.save(beingKeyword);
            nateKeywordRepository.save(nateKeyword);
        }


        return UserResponseDto.of(userRepository.save(user));


    }

    //로그인 하는 메소드
    public TokenDto login(SigninRequestDto requestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        //주입받은 Builder를 통해 AuthenticationManager를 구현한 ProviderManager를 생성한다.
        //이후 ProviderManager는 데이터를 AbstractUserDetailsAuthenticationProvider 의 자식 클래스인 DaoAuthenticationProvider 를 주입받아서 호출
        //DaoAuthenticationProvider 내부에 있는 authenticate에서 retrieveUser을 통해 DB에서의 User의 비밀번호가 실제 비밀번호가 맞는지 비교

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        //retrieveUser에서는 DB에서의 User를 꺼내기 위해, CustomUserDetailService에 있는 loadUserByUsername을 가져와 사용
        return tokenProvider.generateTokenDto(authentication);
    }

}
