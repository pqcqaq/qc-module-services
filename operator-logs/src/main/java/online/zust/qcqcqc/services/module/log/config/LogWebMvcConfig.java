package online.zust.qcqcqc.services.module.log.config;

import lombok.RequiredArgsConstructor;
import online.zust.qcqcqc.services.module.log.interceptor.DebugLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author qcqcqc
 * Date: 2024/4/12
 * Time: 23:49
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "services.log", name = "debug", havingValue = "true")
public class LogWebMvcConfig implements WebMvcConfigurer {

    private final DebugLogInterceptor debugLogInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(debugLogInterceptor).addPathPatterns("/**");
    }
}
