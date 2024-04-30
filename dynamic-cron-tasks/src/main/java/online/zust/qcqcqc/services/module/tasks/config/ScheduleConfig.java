package online.zust.qcqcqc.services.module.tasks.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author qcqcqc
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        //线程池大小
        threadPoolTaskScheduler.setPoolSize(10);
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        //线程名称
        threadPoolTaskScheduler.setThreadNamePrefix("cron-task-");
        // 等待时长
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        return threadPoolTaskScheduler;
    }
}
