package online.zust.qcqcqc.services.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author qcqcqc
 * Date: 2024/5/8
 * Time: 下午3:59
 */
@Slf4j
@Component
public class MbpEnhanceMapperScannerConfigurer extends MapperScannerConfigurer {
    private static final String BASE_PACKAGE = "online.zust.qcqcqc.services.module.**.mapper";
    private static String applicationBasePackage;

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
        Object next = beansWithAnnotation.values().iterator().next();
        MapperScan annotation = next.getClass().getAnnotation(MapperScan.class);
        if (annotation == null) {
            // 如果都没有设置，就取@SpringBootApplication所在的包作为基础包
            String basePackage = next.getClass().getPackage().getName();
            log.info("basePackage from ApplicationPackage: {}", basePackage);
            MbpEnhanceMapperScannerConfigurer.applicationBasePackage = basePackage;
            return;
        }
        String[] strings = annotation.basePackages();
        String[] value = annotation.value();
        Class<?>[] basePackageClassesList = annotation.basePackageClasses();
        // 如果value已经设置，就作为基础包
        if (value.length > 0) {
            String packages = String.join(", ", value);
            log.info("basePackages from MapperScan.value: {}", packages);
            MbpEnhanceMapperScannerConfigurer.applicationBasePackage = packages;
            return;
        }
        // 如果basePackageList已经设置，就作为基础包
        if (strings.length > 0) {
            String packages = String.join(", ", strings);
            log.info("basePackages from MapperScan.basePackages: {}", packages);
            MbpEnhanceMapperScannerConfigurer.applicationBasePackage = packages;
            return;
        }
        // 如果basePackageClassesList已经设置，就作为基础包
        List<String> list = Arrays.stream(basePackageClassesList).map(Class::getPackage).map(Package::getName).toList();
        if (basePackageClassesList.length > 0) {
            String packages = String.join(", ", list);
            log.info("basePackages from MapperScan.basePackageClasses: {}", packages);
            MbpEnhanceMapperScannerConfigurer.applicationBasePackage = packages;
        }
    }
}
