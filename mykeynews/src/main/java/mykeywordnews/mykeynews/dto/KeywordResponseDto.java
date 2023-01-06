package mykeywordnews.mykeynews.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mykeywordnews.mykeynews.domain.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeywordResponseDto {

    private Long userNo;
    private String userId;
    private List<NaverKeyword> naverKeyword;
    private List<NateKeyword> nateKeyword;
    private List<ZumKeyword> zumKeyword;
    private List<BeingKeyword> beingKeyword;

    public static KeywordResponseDto of(User user) {
        return KeywordResponseDto.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .naverKeyword(user.getNaverKeywords())
                .nateKeyword(user.getNateKeywords())
                .zumKeyword(user.getZumKeywords())
                .beingKeyword(user.getBeingKeywords())
                .build();
    }

//    public ZumKeyword toZumKeyword(){
//        return ZumKeyword.builder()
//                .userId(userId)
//                .
//                .authority(Authority.ROLE_USER)
//                .build();
//    }

//    public KeywordResponseDto(User user) {
//        naverKeyword = user.getNaverKeywords();
//        nateKeyword = user.getNateKeywords();
//        beingKeyword = user.getBeingKeywords();
//        zumKeyword = user.getZumKeywords();
//    }
}
