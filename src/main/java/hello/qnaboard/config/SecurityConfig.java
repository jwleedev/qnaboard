package hello.qnaboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 비밀번호 암호화해주는 기계를 스프링 빈으로 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.csrf(csrf -> csrf.disable()) // 로컬 개발 환경을 위해 csrf 공격 방어 기능 임시 비활성화
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 모든 경로에 대해 일단 로그인 없이 접근 허용

                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());
        return http.build();
    }
}
