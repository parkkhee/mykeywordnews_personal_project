package mykeywordnews.mykeynews.controller;

import lombok.RequiredArgsConstructor;
import mykeywordnews.mykeynews.common.util.Thread_crawling_refresh;
import mykeywordnews.mykeynews.dto.KeywordResponseDto;
import mykeywordnews.mykeynews.dto.UserResponseDto;
import mykeywordnews.mykeynews.dto.UserUpdateDto;
import mykeywordnews.mykeynews.service.UserService;
import mykeywordnews.mykeynews.service.WordFrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final WordFrontService wordFrontService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> getMyMemberInfo() {
        UserResponseDto myInfoBySecurity = userService.getMyInfoBySecurity();
        System.out.println(myInfoBySecurity.getUserId());

//        KeywordResponseDto keywordResponseDto = wordFrontService.getMyKeyword();

        return ResponseEntity.ok((myInfoBySecurity));
        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
    }

    @PostMapping("/me/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> userUpdate(@PathVariable String id ,@RequestBody UserUpdateDto userUpdateDto) throws Exception{

        UserResponseDto userResponseDto = userService.userUpdate(id, userUpdateDto);

        if(userUpdateDto.getUserKeyword().get(0) != null && userUpdateDto.getUserKeyword().get(1)!= null && userUpdateDto.getUserKeyword().get(0) != "" && userUpdateDto.getUserKeyword().get(1) != "") {

            Thread_crawling_refresh thr_crawling_refresh = new Thread_crawling_refresh(userUpdateDto, userResponseDto.getUserNo(), 1);
            Thread_crawling_refresh thr_crawling2_refresh = new Thread_crawling_refresh(userUpdateDto, userResponseDto.getUserNo(), 2);
            Thread_crawling_refresh thr_crawling3_refresh = new Thread_crawling_refresh(userUpdateDto, userResponseDto.getUserNo(), 3);
            Thread_crawling_refresh thr_crawling4_refresh = new Thread_crawling_refresh(userUpdateDto, userResponseDto.getUserNo(), 4);

            thr_crawling_refresh.start();
            thr_crawling2_refresh.start();
            thr_crawling3_refresh.start();
            thr_crawling4_refresh.start();

            Thread.sleep(70000);

        }


//        Thread.sleep(100000);

        return ResponseEntity.ok("회원업데이트 성공!");

    }

}
