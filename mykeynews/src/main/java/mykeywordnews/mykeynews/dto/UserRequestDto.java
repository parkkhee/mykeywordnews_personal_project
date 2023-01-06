package mykeywordnews.mykeynews.dto;

import lombok.*;
import mykeywordnews.mykeynews.domain.Authority;
import mykeywordnews.mykeynews.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Request를 받을 때 쓰이는 dto다. UsernamePasswordAuthenticationToken를 반환하여 아이디와 비밀번호가 일치하는지 검증하는 로직을 넣는다
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    @Pattern(regexp="^[a-zA-Z0-9]{4,8}$", message = "아이디를 4~12자로 입력해주세요.(특수문자X)")
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String userId;

    //    @NotBlank(message = "필수 입력 값")
//    @Pattern(regexp = "^[8,14]",message = "")
    @Pattern(regexp="^[a-zA-Z0-9]{6,12}$", message = "비밀번호를 6~12자로 입력해주세요.")
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String userPassword;

    @NotEmpty
    private List<String> userKeyword = new ArrayList<>();

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .userId(userId)
                .userPassword(passwordEncoder.encode(userPassword))
                .authority(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, userPassword);
    }

}
