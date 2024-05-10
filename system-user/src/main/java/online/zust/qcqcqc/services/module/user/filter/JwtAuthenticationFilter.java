package online.zust.qcqcqc.services.module.user.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.entity.dto.Result;
import online.zust.qcqcqc.services.entity.dto.ResultCode;
import online.zust.qcqcqc.services.module.redis.service.RedisService;
import online.zust.qcqcqc.services.module.user.entity.UserLogin;
import online.zust.qcqcqc.services.utils.JwtUtils;
import online.zust.qcqcqc.services.utils.ResponseUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @author qcqcqc
 * <p>
 * 这个过滤器的主要功能：如果token有效，把用户信息存入Security上下文中
 */

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RedisService redisService;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String tokenRow = request.getHeader("Authorization");
        // 如果token为空，则放行
        if (Objects.equals(tokenRow, "null") || StringUtils.isBlank(tokenRow)) {
            filterChain.doFilter(request, response);
            // 不再执行下面的代码
            return;
        }
        String token = tokenRow.replace("Bearer ", "");

        // 检查token是否合法
        boolean b = jwtUtils.verifyJwt(token);
        if (!b) {
            ResponseUtils.renderString(response, Result.error(ResultCode.UNAUTHORIZED.getCode(), "Token has expired, please log in again", "请重新登录"));
            return;
        }
        // 去redis中查询token是否存在 如果不存在，则抛出异常

        // 如果存在，则获取用户信息
        UserLogin userLogin = redisService.get(token, UserLogin.class);
        if (Objects.isNull(userLogin)) {
            ResponseUtils.renderString(response, Result.error(ResultCode.UNAUTHORIZED.getCode(), "Token has expired, please log in again", "请重新登录"));
            return;
        }

        // 存入Security上下文中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLogin.getUser(), null, userLogin.getAuthorities());
        // 存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
