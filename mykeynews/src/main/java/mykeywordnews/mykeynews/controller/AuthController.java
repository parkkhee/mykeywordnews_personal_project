package mykeywordnews.mykeynews.controller;

import lombok.RequiredArgsConstructor;
//import mykeywordnews.mykeynews.dto.KeywordRequestDto;
import lombok.extern.log4j.Log4j;
import mykeywordnews.mykeynews.common.util.Thread_crawling;
import mykeywordnews.mykeynews.dto.SigninRequestDto;
import mykeywordnews.mykeynews.dto.TokenDto;
import mykeywordnews.mykeynews.dto.UserRequestDto;
import mykeywordnews.mykeynews.dto.UserResponseDto;
import mykeywordnews.mykeynews.service.AuthService;
//import mykeywordnews.mykeynews.service.KeywordService;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static mykeywordnews.mykeynews.common.util.ExecPython.execPython;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequestDto requestDto) throws Exception{

        UserResponseDto signupDto = authService.signup(requestDto);


        Thread_crawling thread_crawling = new Thread_crawling(requestDto, signupDto, 1);
        Thread_crawling thread_crawling2 = new Thread_crawling(requestDto, signupDto, 2);
        Thread_crawling thread_crawling3 = new Thread_crawling(requestDto, signupDto, 3);
        Thread_crawling thread_crawling4 = new Thread_crawling(requestDto, signupDto, 4);

        thread_crawling.start();
        thread_crawling2.start();
        thread_crawling3.start();
        thread_crawling4.start();

        Thread.sleep(70000);


        return ResponseEntity.ok(thread_crawling);
    }



    @PostMapping("/api/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody SigninRequestDto requestDto) {

        return ResponseEntity.ok(authService.login(requestDto));
    }
}
