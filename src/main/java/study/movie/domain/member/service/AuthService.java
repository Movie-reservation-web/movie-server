package study.movie.domain.member.service;

import study.movie.auth.dto.TokenResponse;
import study.movie.domain.member.dto.request.LoginRequest;
import study.movie.domain.member.dto.request.OAuth2RegisterRequest;

public interface AuthService {

    /**
     * 1. authentication 생성
     * - request로 들어온 email, password 로 Authentication 객체 생성
     * - 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
     * <p>
     * 2. 검증
     * - 사용자, 비밀번호 체크
     * - authenticate -> CustomUserDetailService(loadUserByUsername)
     * <p>
     * 3. 인증 정보를 기반으로 JWT토큰 생성
     */
    TokenResponse login(LoginRequest request);

    /**
     * Access 토큰이 만료되었을 때 갱신 <P>
     * 1. refresh 토큰 검증 <P>
     * 2. request 의 access 토큰 정보를 가지고 email 추출 <P>
     * 3. 추출된 email 을 key 값으로 가지고 있는 Refresh Token 을 가져옴 <P>
     * 4. Refresh Token null 체크 <P>
     * 5. Refresh Token 과 request 의 refresh token 값 비교 <P>
     * 6. 새로운 token 정보 생성 <P>
     * 7. Redis 에 Refresh 토큰 업데이트
     *
     * @param request
     * @return
     */
    TokenResponse reissue(String refreshTokenRequest);

    /**
     * JwtAuthenticationFilter 에서 doFilter 메서드를 통해 securityContext 에 Authentication 객체가 이미 들어있음<p>
     * 1. SecurityContext 에서 Authentication 가져옴. (이미 검증은 되어있음)<p>
     * 2. Redis 에 Authentication 정보를(RT : email 형태) 키 값으로 가지고 있는 token 이 있으면
     * 해당 키값 삭제 <p>
     * 3. HttpServletRequest Header 에서 Access Token 의 만료시간을 가져옴
     * 4. Redis 에 Acces Token을 키 값으로 가지는 블랙 리스트 추가
     * 5. SecurityContext 에 있는 authentication 객체를 삭제.
     * @param request
     */
    void logout(String accessTokenRequest);

    TokenResponse socialRegister(OAuth2RegisterRequest request);
}
