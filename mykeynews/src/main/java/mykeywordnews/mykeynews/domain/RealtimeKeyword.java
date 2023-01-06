package mykeywordnews.mykeynews.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealtimeKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rtkeywordNo;

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


}
