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
public class BeingKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordNo;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private User user;

    private String userKey;

    @Column(length = 800)
    private String beingNewsUrl1;
    private String beingNewsWord1_1;
    private String beingNewsWord1_2;

    @Column(length = 800)
    private String beingNewsUrl2;
    private String beingNewsWord2_1;
    private String beingNewsWord2_2;

    @Column(length = 800)
    private String beingNewsUrl3;
    private String beingNewsWord3_1;
    private String beingNewsWord3_2;

    @Column(length = 800)
    private String beingNewsUrl4;
    private String beingNewsWord4_1;
    private String beingNewsWord4_2;

    private Integer beingWordCnt1;
    private Integer beingWordCnt2;
    private Integer beingWordCnt3;
    private Integer beingWordCnt4;

    @Column(columnDefinition = "LONGTEXT")
    private String beingSummary1;
    @Column(columnDefinition = "LONGTEXT")
    private String beingSummary2;
    @Column(columnDefinition = "LONGTEXT")
    private String beingSummary3;
    @Column(columnDefinition = "LONGTEXT")
    private String beingSummary4;


    public BeingKeyword(String userKey) {
        this.userKey = userKey;
    }

    //==연관관계 메서드==// -> 양방향 관계에서 사용한다.
    public void setUser(User user) {
        this.user = user;
        user.getBeingKeywords().add(this);
    }
}
