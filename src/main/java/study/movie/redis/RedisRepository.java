package study.movie.redis;

import study.movie.auth.dto.TokenResponse;

public interface RedisRepository {
    void save(String key, TokenResponse response);
    void save(String key, String value, long expiration);
    void delete(String key);
    String getValue(String key);
    boolean hasKey(String key);
}
