package online.zust.qcqcqc.services.module.log;

import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import online.zust.qcqcqc.utils.utils.ProxyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author qcqcqc
 * @date 2024/4/8
 * @time 10:50
 */
@SpringBootTest(classes = LoggerApplication.class)
public class TestOperationLog {

    @Configuration
    static class config {
        @Bean
        public Long userId() {
            return 1L;
        }
    }

    @Component
    static class Tests {
        @OperationLog("")
        public void success() {
            System.out.println("testSuccess");
        }

        @OperationLog("")
        public void fail() {
            System.out.println("testFail");
            throw new ServiceException("testFail");
        }
    }

    @Test
    public void doTest() {
        Tests selfProxied = ProxyUtil.getBean(Tests.class);
        selfProxied.success();
        selfProxied.fail();
    }
}
