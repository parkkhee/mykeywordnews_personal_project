package mykeywordnews.mykeynews.service;

import lombok.RequiredArgsConstructor;
import mykeywordnews.mykeynews.common.util.SecurityUtil;
import mykeywordnews.mykeynews.common.util.Thread_crawling;
import mykeywordnews.mykeynews.common.util.Thread_crawling_refresh;
import mykeywordnews.mykeynews.domain.*;
import mykeywordnews.mykeynews.dto.UserResponseDto;
import mykeywordnews.mykeynews.dto.UserUpdateDto;
import mykeywordnews.mykeynews.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ZumKeywordRepository zumKeywordRepository;
    private final BeingKeywordRepository beingKeywordRepository;
    private final NateKeywordRepository nateKeywordRepository;
    private final KeywordRepository keywordRepository;

    //getMyInfoBySecurity는 헤더에 있는 token값을 토대로 Member의 data를 건내주는 메소드
    public UserResponseDto getMyInfoBySecurity() {
        return userRepository.findByUserId(SecurityUtil.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public UserResponseDto userUpdate(String id, UserUpdateDto userUpdateDto) throws Exception{
        Optional<User> byId = userRepository.findByUserId(id);
        User user = byId.get();


        List<String> userKeyword = userUpdateDto.getUserKeyword();

        if(userKeyword.get(0) != null && userKeyword.get(1)!= null && userKeyword.get(0) != "" && userKeyword.get(1) != ""){


            for (int num=0; num<2; num++){

                Optional<ZumKeyword> zumKeywordOptional = zumKeywordRepository.findById(user.getZumKeywords().get(num).getKeywordNo());
                ZumKeyword zumKeyword = zumKeywordOptional.get();
                Optional<NateKeyword> nateKeywordOptional = nateKeywordRepository.findById(user.getNateKeywords().get(num).getKeywordNo());
                NateKeyword nateKeyword = nateKeywordOptional.get();
                Optional<NaverKeyword> keywordOptional = keywordRepository.findById(user.getNaverKeywords().get(num).getKeywordNo());
                NaverKeyword naverKeyword = keywordOptional.get();
                Optional<BeingKeyword> beingKeywordOptional = beingKeywordRepository.findById(user.getBeingKeywords().get(num).getKeywordNo());
                BeingKeyword beingKeyword = beingKeywordOptional.get();


                user.getNaverKeywords().get(num).setUserKey(userKeyword.get(num));
                naverKeyword.setUser(user);
                keywordRepository.save(naverKeyword);

                user.getNateKeywords().get(num).setUserKey(userKeyword.get(num));
                nateKeyword.setUser(user);
                nateKeywordRepository.save(nateKeyword);


                user.getZumKeywords().get(num).setUserKey(userKeyword.get(num));
                zumKeyword.setUser(user);
                zumKeywordRepository.save(zumKeyword);

                user.getBeingKeywords().get(num).setUserKey(userKeyword.get(num));
                beingKeyword.setUser(user);
                beingKeywordRepository.save(beingKeyword);


            }


            userRepository.save(user);


        }

        if(userUpdateDto.getUserPassword()!=null && userUpdateDto.getUserPassword() !="" && (userUpdateDto.getUserPassword().length() > 5))
            user.setUserPassword(passwordEncoder.encode(userUpdateDto.getUserPassword()));

        return UserResponseDto.of(userRepository.save(user));
    }


}
