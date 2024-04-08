package online.zust.qcqcqc.services.module.log;

import online.zust.qcqcqc.services.exception.ServiceException;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

/**
 * @author qcqcqc
 * @date 2024/4/8
 * @time 10:50
 */
@SpringBootTest
public class TestOperationLog {

    @Bean
    public Long userId() {
        return 1L;
    }

    @OperationLog("")
    public void fail() {
        System.out.println("testFail");
        throw new ServiceException("testFail");
    }

    @OperationLog("")
    public void success() {
        System.out.println("testSuccess");
    }

    @Test
    public void doTest() {
        success();
        fail();
    }
}
