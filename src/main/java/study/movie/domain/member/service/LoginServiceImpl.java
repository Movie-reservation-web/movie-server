package study.movie.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.movie.auth.jwt.JwtTokenProvider;
import study.movie.auth.dto.TokenRequest;
import study.movie.auth.dto.TokenResponse;
import study.movie.exception.CustomException;
import study.movie.global.utils.BasicServiceUtil;
import study.movie.domain.member.dto.request.LoginRequest;
import study.movie.domain.member.repository.MemberRepository;
import study.movie.redis.RedisRepository;

import static org.springframework.util.StringUtils.hasText;
import static study.movie.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginServiceImpl extends BasicServiceUtil implements LoginService {
    private static final String BLACK_LIST_VALUE = "LOGOUT_TOKEN";

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisRepository redisRepository;

    public TokenResponse login(LoginRequest request) {
        // request로 온 email을 가지고 해당 회원이 있는지 확인
        if (memberRepository.existsByEmail(request.getEmail()))
            throw new CustomException(MEMBER_NOT_FOUND);

        // email, pw 로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();

        /*
          AuthenticationManagerBuilder 의 authenticate 메서드가 호출되면
          CustomUserDetailService 에서 override 한 loadUserByUsername 이 호출됨.
          loadUserByUsername 으로 가져온 UserDetails 객체를 가지고 password check 를 한다.
         */
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // 인증 정보를 기반으로 토큰(access, refresh, expiration) 생성
        TokenResponse tokenResponse = jwtTokenProvider.generateToken(authentication);

        // RefreshToken Redis 저장, expirationTime 이 지나면 자동 삭제
        redisRepository.save(jwtTokenProvider.getRefreshTokenKey(authentication), tokenResponse);
        return tokenResponse;
    }

    public TokenResponse reissue(TokenRequest request) {
        // Refresh 토큰 검증
        if (isInvalidationToken(request.getRefreshToken()))
            throw new CustomException(INVALID_REFRESH_TOKEN);

        // 토큰에서 회원 정보 가져오기 :: email
        Authentication authentication = getEmailByAuthentication(request.getAccessToken());

        // Redis 에서 'RT:'+email 을 key 값으로 하는 value 를 가져옴
        String key = jwtTokenProvider.getRefreshTokenKey(authentication);
        String refreshToken = redisRepository.getValue(key);

        // logout 되어 Redis 에 refreshToken 이 없는 경우 체크
        if (!hasText(refreshToken))
            throw new CustomException(REFRESH_TOKEN_NOT_FOUND);

        // Redis 에 저장되어 있는 RefreshToken 정보와 request 의 RefreshToken 정보 비교
        if (!refreshToken.equals(request.getRefreshToken()))
            throw new CustomException(MISMATCH_REFRESH_TOKEN);

        // 새로운 토큰 생성
        TokenResponse tokenResponse = jwtTokenProvider.generateToken(authentication);

        // Refresh 토큰 업데이트
        redisRepository.save(key, tokenResponse);

        return tokenResponse;
    }

    public void logout(TokenRequest request) {
        String accessToken = request.getAccessToken();

        // Access 토큰 검증
        if (isInvalidationToken(accessToken)) {
            throw new CustomException(INVALID_ACCESS_TOKEN);
        }

        // 토큰에서 회원 정보 가져오기 :: email
        Authentication authentication = getEmailByAuthentication(accessToken);

        // 위의 정보(email)로 저장된 refresh token 이 redis 에 있을 경우 삭제
        String key = jwtTokenProvider.getRefreshTokenKey(authentication);
        if (redisRepository.hasKey(key)) redisRepository.delete(key);

        // Access 토큰의 유효시간을 가져옴
        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        /*
            블랙리스트에 저장
            Redis 에 저장되는 key 값: accessToken / value: LOGOUT_TOKEN / expire: accessToken 의 만료시간
         */
        redisRepository.save(accessToken, BLACK_LIST_VALUE, expiration);
    }

    /**
     * Access Token 을 복호화해서 email 정보 가져오기
     * Authentication 객체에 담음
     *
     * @param accessToken
     * @return
     */
    private Authentication getEmailByAuthentication(String accessToken) {
        return jwtTokenProvider.getAuthentication(accessToken);
    }


    /**
     * 유효하지 않은 토큰인지 확인
     *
     * @param token
     * @return
     */
    private boolean isInvalidationToken(String token) {
        return !jwtTokenProvider.validateToken(token);
    }
}
