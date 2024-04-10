package online.zust.qcqcqc.services.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qcqcqc
 * Date: 2024/4/10
 * Time: 15:37
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Value("${thread.pool.core-poll-size:2}")
    private Integer corePollSize;
    @Value("${thread.pool.max-poll-size:10}")
    private Integer maxPollSize;
    @Value("${thread.pool.queue-capacity:100}")
    private Integer queueCapacity;
    @Value("${thread.pool.keep-alive-seconds:60}")
    private Integer keepAliveSeconds;
    @Value("${thread.pool.thread-name-prefix:async-}")
    private String threadNamePrefix;

    @Bean("qc-async")
    public TaskExecutor threadPoolTaskExecutor() {
        log.info("start taskExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(corePollSize);
        // 最大线程数
        executor.setMaxPoolSize(maxPollSize);
        // 任务队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程的最大空闲时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程名前缀
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略：当线程池达到最大线程数时，如何处理新任务
        // CALLER_RUNS：在添加到线程池失败时会由主线程自己来执行这个任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return new SecurityContextExecutor(executor);
    }

}
