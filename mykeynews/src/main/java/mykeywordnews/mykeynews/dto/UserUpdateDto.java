package mykeywordnews.mykeynews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mykeywordnews.mykeynews.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {

    private String userPassword=null;
    private List<String> userKeyword = new ArrayList<>();

//    public UserUpdateDto toEntity(PasswordEncoder passwordEncoder) {
//        return UserUpdateDto.builder()
//                .userPassword(passwordEncoder.encode(userPassword))
//                .build();
//    }

}
