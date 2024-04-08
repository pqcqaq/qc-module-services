package online.zust.qcqcqc.services.module.log;

import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author qcqcqc
 * @date 2024/4/8
 * @time 10:50
 */
@SpringBootTest
public class TestOperationLog {

    @OperationLog("#{test}")
    public void test(String test) {
        System.out.println(test);
    }
}s
