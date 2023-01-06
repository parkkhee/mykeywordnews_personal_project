package mykeywordnews.mykeynews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mykeywordnews.mykeynews.domain.RealtimeKeyword;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RealtimeKeywordDto {
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String keyword4;
    private String keyword5;
    private String keyword6;
    private String keyword7;
    private String keyword8;
    private String keyword9;
    private String keyword10;

    public void RealtimeKeyw(RealtimeKeyword realtimeKeyword){
        this.keyword1 = realtimeKeyword.getKeyword1();
        this.keyword2 = realtimeKeyword.getKeyword2();
        this.keyword3 = realtimeKeyword.getKeyword3();
        this.keyword4 = realtimeKeyword.getKeyword4();
        this.keyword5 = realtimeKeyword.getKeyword5();
        this.keyword6 = realtimeKeyword.getKeyword6();
        this.keyword7 = realtimeKeyword.getKeyword7();
        this.keyword8 = realtimeKeyword.getKeyword8();
        this.keyword9 = realtimeKeyword.getKeyword9();
        this.keyword10 = realtimeKeyword.getKeyword10();
    }


}
