package mykeywordnews.mykeynews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//비밀번호를 수정할 때 쓰이는 dto다. 이전의 비밀번호가 제대로 입력하지 않는다면 실행되지 않는다.
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDto {
    private String userId;
    private String exPassword;
    private String newPassword;
}
