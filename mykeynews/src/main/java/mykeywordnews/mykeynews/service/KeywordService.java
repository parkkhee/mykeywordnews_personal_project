//package mykeywordnews.mykeynews.service;
//
//import lombok.RequiredArgsConstructor;
//import mykeywordnews.mykeynews.domain.Keyword;
//import mykeywordnews.mykeynews.dto.KeywordRequestDto;
//import mykeywordnews.mykeynews.repository.KeywordRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KeywordService {
//    private final KeywordRepository keywordRepository;
//    private final KeywordRequestDto keywordRequestDto;
//
//    public void save(String userId, String userKeyword ) {
//
//        Keyword keyword = keywordRequestDto.toEntity(userId,userKeyword);
//        keywordRepository.save(keyword);
//
//
//    }
//
//}
