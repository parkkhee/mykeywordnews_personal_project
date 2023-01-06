package mykeywordnews.mykeynews.service;

import lombok.RequiredArgsConstructor;
import mykeywordnews.mykeynews.common.util.SecurityUtil;
import mykeywordnews.mykeynews.domain.RealtimeKeyword;
import mykeywordnews.mykeynews.domain.User;
import mykeywordnews.mykeynews.dto.*;
import mykeywordnews.mykeynews.repository.KeywordRepository;
import mykeywordnews.mykeynews.repository.RealtimeKeywordRepository;
import mykeywordnews.mykeynews.repository.UserRepository;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordFrontService {
    private final UserRepository userRepository;
    private final KeywordRepository KeywordRepository;
    private final RealtimeKeywordRepository realtimeKeywordRepository;

    public KeywordResponseDto getMyKeyword() {
        System.out.println(SecurityUtil.getCurrentUserId());
//        Optional<User> user = userRepository.findByUserId(SecurityUtil.getCurrentUserId());

        Optional<User> byUserId = userRepository.findByUserId(SecurityUtil.getCurrentUserId());
        User user = byUserId.get();
//        System.out.println(user.getZumKeywords());
//        System.out.println(user.getNateKeywords());
//        Long userNo = user.getUserNo();
        System.out.println(user.getZumKeywords());
        System.out.println(user.getNateKeywords());
        System.out.println(user.getNaverKeywords());
        System.out.println(user.getBeingKeywords());

        return userRepository.findById(user.getUserNo())   //SecurityUtil.getCurrentUserId()
                .map(KeywordResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));

//        return new KeywordResponseDto(user);
    }

    public RealtimeKeywordDto getRealtime(){

        RealtimeKeyword byRealtimeKeyword = realtimeKeywordRepository.findByRtkeywordNo(Long.valueOf(1));

        RealtimeKeywordDto realtimeKeywordDto = new RealtimeKeywordDto();
        realtimeKeywordDto.RealtimeKeyw(byRealtimeKeyword);
        return realtimeKeywordDto;

    }

    public SummaryRequestDto getSummary(SummaryRequestDto summaryRequestDto) throws IOException, InterruptedException{

//
//        List<String> command = new ArrayList<>();
//        command.add("python");
//        command.add("C:/Users/root/Desktop/MykeywordProject/pythonfile/crawling.py");
//        command.add("\"" + summaryRequestDto.getText() + "\"");
        SummaryRequestDto summaryResponseDto = new SummaryRequestDto();

        //python으로 실행
        CommandLine commandLine = CommandLine.parse("python");
        commandLine.addArgument("C:/Users/root/Desktop/MykeywordProject/pythonfile/summaryAPI.py");
        commandLine.addArgument("\"" + summaryRequestDto.getText() + "\"");


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        executor.execute(commandLine);
        summaryResponseDto.setReText(outputStream.toString());
//        System.out.println("result: " + result);
        System.out.println("output: " + outputStream.toString());

        return summaryResponseDto;
    }
}
