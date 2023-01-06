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
public class NateKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordNo;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private User user;

    private String userKey;

    private String nateNewsUrl1;
    private String nateNewsWord1_1;
    private String nateNewsWord1_2;

    private String nateNewsUrl2;
    private String nateNewsWord2_1;
    private String nateNewsWord2_2;

    private String nateNewsUrl3;
    private String nateNewsWord3_1;
    private String nateNewsWord3_2;

    private String nateNewsUrl4;
    private String nateNewsWord4_1;
    private String nateNewsWord4_2;

    private Integer nateWordCnt1;
    private Integer nateWordCnt2;
    private Integer nateWordCnt3;
    private Integer nateWordCnt4;

    @Column(columnDefinition = "LONGTEXT")
    private String nateSummary1;
    @Column(columnDefinition = "LONGTEXT")
    private String nateSummary2;
    @Column(columnDefinition = "LONGTEXT")
    private String nateSummary3;
    @Column(columnDefinition = "LONGTEXT")
    private String nateSummary4;


    public NateKeyword(String userKey) {
        this.userKey = userKey;
    }

    //==연관관계 메서드==// -> 양방향 관계에서 사용한다.
    public void setUser(User user) {
        this.user = user;
        user.getNateKeywords().add(this);
    }
}
