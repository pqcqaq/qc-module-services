package online.zust.qcqcqc.services.module.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author qcqcqc
 * Date: 2024/5/11
 * Time: 上午12:07
 */
@Configuration
@Slf4j
public class SystemSecurityConfig {

    /**
     * 表达式处理器
     *
     * @param roleHierarchy 角色继承
     * @return DefaultMethodSecurityExpressionHandler
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RoleHierarchy.class)
    DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        log.info("RoleHierarchy set successfully!");
        return expressionHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 设置中文配置
        messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
        log.info("messageSource set successfully!");
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        log.info("PasswordEncoder set successfully! like BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        log.info("AuthenticationManager set successfully!");
        return authenticationConfiguration.getAuthenticationManager();
    }
}
