package mykeywordnews.mykeynews.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import mykeywordnews.mykeynews.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider {

    //AUTHORITIES_KEY와 BEARER_TYPE은 토큰을 생성하고 검증할 때 쓰이는 string값
    private static final String AUTHORITES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    //토큰의 만료 시간
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    //JWT 를 만들 때 사용하는 암호화 키값을 사용하기 위해 security에서 불러왔다
    private final Key key;

    // 주의점: 여기서 @Value는 `springframework.beans.factory.annotation.Value`소속이다! lombok의 @Value와 착각하지 말것!
    //     * @param secretKey
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        // Authentication 인터페이스를 확장한 매개변수를 받아서 그 값을 string으로 변환한다
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        System.out.println(tokenExpiresIn);

        //현재 시각과 만료시각 을 Token에 build한다
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITES_KEY, authorities)
                .setExpiration(tokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        //tokenDto에 token의 정보를 담는다
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .tokenExpiresIn(tokenExpiresIn.getTime())
                .build();
    }

    //토큰의 인증을 꺼내는 메소드
    public Authentication getAuthentication(String accessToken) {
        //parseClaims 메소드로 string 형태의 토큰을 claims형태로 생성
        Claims claims = parseClaims(accessToken);

        //auth가 없을시 exception반환
        if (claims.get(AUTHORITES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //GrantedAuthority을 상속받은 타입만이 사용 가능한 Collection을 반환
        Collection<? extends GrantedAuthority> authorities =
                //stream을 통한 함수형 프로그래밍으로 claims형태의 토큰을 알맞게 정렬한 이후
                // SimpleGrantedAuthority형태의 새 List를 생성한다. 여기에는 인가가 들어있다.
                Arrays.stream(claims.get(AUTHORITES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        //UserDetails를 생성해서 후에 SecurityContext에 사용하기 위해 만든, 왜냐하면 SecurityContext는 Authentication객체를 저장
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //토큰을 검증하기 위한 메소드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    //토큰을 claims형태로 만드는 메소드다. 이를 통해 위에서 권한 정보가 있는지 없는지 체크가 가능
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
