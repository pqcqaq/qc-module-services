package online.zust.qcqcqc.services.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author qcqcqc
 */
@Component
@Slf4j
public class JwtUtils {
    /**
     * token密钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * token过期时间,单位：ms
     */
    @Value("${token.expiration:3600000}")
    private int expireTime;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    /**
     * 生成token
     *
     * @param info 用户信息
     * @return token
     */
    public <T> String createJwt(T info) {
        JwtBuilder builder = Jwts.builder();
        String s;
        try {
            s = objectMapper.writeValueAsString(info);
        } catch (Exception e) {
            log.error("生成token失败,{}", e.getMessage());
            throw new RuntimeException("生成token失败");
        }
        return builder
                //头
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //载荷
                //普通属性（用户名，角色等）
                .claim("info", s)
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                //id
                .setId(UUID.randomUUID().toString())
                //签名
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 验证token
     *
     * @param token token
     * @return 是否有效
     */
    public boolean verifyJwt(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取token载荷
     *
     * @param token     token
     * @param valueType 类型
     * @param <T>       类型
     * @return 载荷
     */
    public <T> T getPayload(String token, Class<T> valueType) {
        try {
            String info = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("info", String.class);
            return objectMapper.readValue(info, valueType);
        } catch (Exception e) {
            return null;
        }
    }
}

