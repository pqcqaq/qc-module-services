package online.zust.qcqcqc.services.module.user.config;

import online.zust.qcqcqc.services.module.user.filter.JwtAuthenticationFilter;
import online.zust.qcqcqc.services.module.user.service.impl.UserDetailsServiceImpl;
import online.zust.qcqcqc.services.module.user.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/5/11
 * Time: 上午12:04
 */
@Configuration
@Import({
        JwtAuthenticationFilter.class,
        UserDetailsServiceImpl.class,
        UserServiceImpl.class,
        SystemSecurityConfig.class
})
public class UserServiceAutoInject {
}
