package mykeywordnews.mykeynews.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(nullable = false, length = 7, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    @JsonIgnore
    @OneToMany(targetEntity = NaverKeyword.class, mappedBy = "user", fetch=FetchType.LAZY, orphanRemoval = true)
    private List<NaverKeyword> naverKeywords = new ArrayList<>();;

    @JsonIgnore
    @OneToMany(targetEntity = ZumKeyword.class, mappedBy = "user", fetch=FetchType.LAZY, orphanRemoval = true)
    private List<ZumKeyword> zumKeywords = new ArrayList<>();

    @JsonIgnore
    @OneToMany(targetEntity = BeingKeyword.class, mappedBy = "user", fetch=FetchType.LAZY, orphanRemoval = true)
    private List<BeingKeyword> beingKeywords = new ArrayList<>();

    @JsonIgnore
    @OneToMany(targetEntity = NateKeyword.class, mappedBy = "user", fetch=FetchType.LAZY, orphanRemoval = true)
    private List<NateKeyword> nateKeywords = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String userId, String userPassword, Authority authority){
        this.userId = userId;
        this.userPassword = userPassword;
        this.authority = authority;
    }


}
