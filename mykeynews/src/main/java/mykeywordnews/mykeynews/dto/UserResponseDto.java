package mykeywordnews.mykeynews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mykeywordnews.mykeynews.domain.NaverKeyword;
import mykeywordnews.mykeynews.domain.User;
import mykeywordnews.mykeynews.domain.ZumKeyword;

import java.util.ArrayList;
import java.util.List;

/**
 * Response를 보낼때 쓰이는 dto다.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long userNo;
    private String userId;
    private String userKey1;
    private String userKey2;
//    private List<NaverKeyword> userKeyword;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userKey1(user.getNateKeywords().get(0).getUserKey())
                .userKey2(user.getNateKeywords().get(1).getUserKey())
//                .userKeyword(user.getNaverKeywords())
                .build();
    }
}
