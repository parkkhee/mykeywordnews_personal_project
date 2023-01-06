package mykeywordnews.mykeynews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mykeywordnews.mykeynews.domain.Authority;
import mykeywordnews.mykeynews.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SigninRequestDto {

    private String userId;

    //    @NotBlank(message = "필수 입력 값")
//    @Pattern(regexp = "^[8,14]",message = "")
    private String userPassword;

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
