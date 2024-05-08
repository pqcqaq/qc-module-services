package online.zust.qcqcqc.services.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qcqcqc
 * Date: 2024/5/8
 * Time: 下午3:59
 */
@Slf4j
@Component
public class MbpEnhanceMapperScannerConfigurer extends MapperScannerConfigurer {
    private static String applicationBasePackage;
    private static final String BASE_PACKAGE = "online.zust.qcqcqc.services.module.**.mapper";

    @Override
    public void afterPropertiesSet() throws Exception {
        String packages = applicationBasePackage + ", " + BASE_PACKAGE;
        setBasePackage(packages);
        log.info("basePackages: {}", packages);
        super.afterPropertiesSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
        if (beansWithAnnotation.isEmpty()) {
            throw new RuntimeException("SpringBootApplication annotation not found");
        }
        String basePackage = beansWithAnnotation.values().iterator().next().getClass().getPackage().getName();
        log.info("basePackage: {}", basePackage);
        MbpEnhanceMapperScannerConfigurer.applicationBasePackage = basePackage;
    }
}
