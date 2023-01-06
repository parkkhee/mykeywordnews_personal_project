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
public class ZumKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordNo;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "userNo")
    private User user;

    private String userKey;

    private String zumNewsUrl1;
    private String zumNewsWord1_1;
    private String zumNewsWord1_2;

    private String zumNewsUrl2;
    private String zumNewsWord2_1;
    private String zumNewsWord2_2;

    private String zumNewsUrl3;
    private String zumNewsWord3_1;
    private String zumNewsWord3_2;

    private String zumNewsUrl4;
    private String zumNewsWord4_1;
    private String zumNewsWord4_2;


    private Integer zumWordCnt1;
    private Integer zumWordCnt2;
    private Integer zumWordCnt3;
    private Integer zumWordCnt4;

    @Column(columnDefinition = "LONGTEXT")
    private String zumSummary1;
    @Column(columnDefinition = "LONGTEXT")
    private String zumSummary2;
    @Column(columnDefinition = "LONGTEXT")
    private String zumSummary3;
    @Column(columnDefinition = "LONGTEXT")
    private String zumSummary4;


    public ZumKeyword(String userKey) {
        this.userKey = userKey;
    }

    //==연관관계 메서드==// -> 양방향 관계에서 사용한다.
    public void setUser(User user) {
        this.user = user;
        user.getZumKeywords().add(this);
    }

}
