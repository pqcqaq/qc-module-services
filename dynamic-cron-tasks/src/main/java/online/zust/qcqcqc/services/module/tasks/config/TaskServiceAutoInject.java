package online.zust.qcqcqc.services.module.tasks.config;

import online.zust.qcqcqc.services.module.tasks.services.impl.DynamicTaskServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午8:45
 */
@Configuration
@Import({
        ScheduleConfig.class,
        DynamicTaskServiceImpl.class
})
public class TaskServiceAutoInject {
}
