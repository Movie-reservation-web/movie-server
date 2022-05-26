package study.movie.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import study.movie.auth.dto.TokenResponse;
import study.movie.exception.CustomException;
import study.movie.global.config.AppProperties;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static study.movie.exception.ErrorCode.PERMISSION_NOT_ACCESSIBLE;

@Slf4j
@Component
public class JwtTokenProvider {
    private Key key;
    private final AppProperties appProperties;
    private static final String AUTHORITIES_KEY = "auth";

    public JwtTokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
        byte[] keyBites = Decoders.BASE64.decode(appProperties.getAuth().getTokenSecret());
        this.key = Keys.hmacShaKeyFor(keyBites);
    }

    /**
     * 회원 정보를 가지고 Access Token, Refresh Token 생성
     *
     * @param authentication
     * @param response
     * @return TokenResponse
     */
    public TokenResponse generateToken(Authentication authentication) {
        // 현재 시간
        Date now = new Date();

        // access token 생성
        String accessToken = this.createAccessToken(authentication, now);

        // refresh token 생성
        String refreshToken = this.createRefreshToken(now);

        // 생성한 Token 정보를 Response 에 담아 리턴
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 토큰 정보 검증
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * token 정보 복호화
     *
     * @param accessToken
     * @return
     */
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 복호화한 Claim 에 권한 정보가 없으면 예외 발생
        if (claims.get(AUTHORITIES_KEY) == null)
            throw new CustomException(PERMISSION_NOT_ACCESSIBLE);

        // 복호화한 Claim 에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = convertToAuthorities(claims.get(AUTHORITIES_KEY).toString());

        // UserDetails 객체를 만들어 Authentication 리턴
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * Access Token 의 만료 시간 리턴
     *
     * @param accessToken
     * @return
     */
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        Long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    /**
     * 토큰을 복호화 하여 토큰정보(claims)를 리턴
     *
     * @param accessToken
     * @return
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * Authentication 객체에서 authorities 가져와 토큰에 담을 수 있게 문자열로 변환
     *
     * @param authentication
     * @return
     */
    private String convertToString(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * 문자열로 변환되어 있던 authorities 정보를 Collection 으로 변환
     *
     * @param data
     * @return
     */
    private Collection<? extends GrantedAuthority> convertToAuthorities(String data) {
        return Arrays.stream(data.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 만료 시간 계산
     *
     * @param date
     * @param tokenExpireTime
     * @return
     */
    private Date getExpireTime(Date date, long tokenExpireTime) {
        return new Date(date.getTime() + tokenExpireTime);
    }

    /**
     * Access Token 생성
     *
     * @param authentication
     * @param email
     * @param expiration
     * @return
     */
    public String createAccessToken(Authentication authentication, Date date) {
        // 만료 시간 설정
        Date accessTokenExpiration = this.getExpireTime(date, appProperties.getAuth().getAccessTokenExpireTime());

        // 권한 가져오기
        String authorities = this.convertToString(authentication);

        return Jwts.builder()
                .setSubject(authentication.getName()) // email
                .claim(AUTHORITIES_KEY, authorities) // 권한 정보
                .setExpiration(accessTokenExpiration) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param expiration
     * @return
     */
    private String createRefreshToken(Date date) {
        Date refreshTokenExpiration = this.getExpireTime(date, appProperties.getAuth().getRefreshTokenExpireTime());
        return Jwts.builder()
                .setExpiration(refreshTokenExpiration) // 만료시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh Token 저장에 사용될 key 값 생성
     * -> 'RT:' + '로그인할 때 사용된 ID(Member email)'
     *
     * @param authentication
     * @return
     */
    public String getRefreshTokenKey(Authentication authentication) {
        return "RT:" + authentication.getName();
    }


}
