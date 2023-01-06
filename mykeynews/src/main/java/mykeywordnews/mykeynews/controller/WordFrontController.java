package mykeywordnews.mykeynews.controller;

import lombok.RequiredArgsConstructor;
import mykeywordnews.mykeynews.dto.KeywordResponseDto;
import mykeywordnews.mykeynews.dto.RealtimeKeywordDto;
import mykeywordnews.mykeynews.dto.SigninRequestDto;
import mykeywordnews.mykeynews.dto.SummaryRequestDto;
import mykeywordnews.mykeynews.service.WordFrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class WordFrontController {
    private final WordFrontService wordFrontService;


    @GetMapping("/wordfront")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<KeywordResponseDto> getMyKeyword(Authentication authentication) throws Exception{
        if (authentication == null) {
            throw new BadCredentialsException("회원 정보를 찾을 수 없습니다.");
        }
        KeywordResponseDto keywordResponseDto = wordFrontService.getMyKeyword();
        return ResponseEntity.ok(keywordResponseDto);

    }

    @GetMapping("/realtime")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> getRealtimeKeyword() throws  Exception{
        RealtimeKeywordDto realtimeKeywordDto = wordFrontService.getRealtime();
        return ResponseEntity.ok(realtimeKeywordDto);

    }

    @PostMapping("/summary")
    public ResponseEntity<?> postSummary(@RequestBody SummaryRequestDto summaryRequestDto) throws Exception{
        SummaryRequestDto summaryResponseDto = wordFrontService.getSummary(summaryRequestDto);
        return ResponseEntity.ok(summaryResponseDto);
    }

}
