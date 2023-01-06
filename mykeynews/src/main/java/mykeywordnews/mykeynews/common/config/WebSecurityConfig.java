package mykeywordnews.mykeynews.common.config;

import lombok.RequiredArgsConstructor;
import mykeywordnews.mykeynews.common.jwt.JwtAccessDeniedHandler;
import mykeywordnews.mykeynews.common.jwt.JwtAuthenticationEntryPoint;
import mykeywordnews.mykeynews.common.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    //request로부터 받은 비밀번호를 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()  //https만을 사용하기위해 httpBasic을 disable
                .csrf().disable()  //우리는 리액트에서 token을 localstorage에 저장할 것이기 때문에 csrf 방지또한 disable
                //REST API를 통해 세션 없이 토큰을 주고받으며 데이터를 주고받기 때문에 세션설정또한 STATELESS
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //예외를 핸들링
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                ////Requests에 있어서 /auth/**를 제외한 모든 uri의 request는 토큰이 필요
                .and()
                .authorizeRequests()
                .antMatchers("/auth/signup","/auth/api/**").permitAll()   //"/auth/**","/**"
                .anyRequest().authenticated()

                //JwtSecurityConfig클래스를 통해 tokenProvider를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}
