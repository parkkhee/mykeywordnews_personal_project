package mykeywordnews.mykeynews.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NaverKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordNo;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private User user;

    private String userKey;

    private String naverNewsUrl1;
    private String naverNewsWord1_1;
    private String naverNewsWord1_2;

    private String naverNewsUrl2;
    private String naverNewsWord2_1;
    private String naverNewsWord2_2;

    private String naverNewsUrl3;
    private String naverNewsWord3_1;
    private String naverNewsWord3_2;

    private String naverNewsUrl4;
    private String naverNewsWord4_1;
    private String naverNewsWord4_2;

    private Integer naverWordCnt1;
    private Integer naverWordCnt2;
    private Integer naverWordCnt3;
    private Integer naverWordCnt4;

    @Column(columnDefinition = "LONGTEXT")
    private String naverSummary1;
    @Column(columnDefinition = "LONGTEXT")
    private String naverSummary2;
    @Column(columnDefinition = "LONGTEXT")
    private String naverSummary3;
    @Column(columnDefinition = "LONGTEXT")
    private String naverSummary4;


    public NaverKeyword(String userKey) {
        this.userKey = userKey;
    }

    //==연관관계 메서드==// -> 양방향 관계에서 사용한다.
    public void setUser(User user) {
        this.user = user;
        user.getNaverKeywords().add(this);
    }
}
