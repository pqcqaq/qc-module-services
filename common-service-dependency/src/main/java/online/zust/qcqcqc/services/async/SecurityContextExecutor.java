package online.zust.qcqcqc.services.async;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author qcqcqc
 * 解决子线程异步任务中无法获取SecurityContext的问题
 * Date: 2024/4/10
 * Time: 11:34
 */
@Slf4j
public class SecurityContextExecutor implements TaskExecutor {

    private final TaskExecutor executor;

    public SecurityContextExecutor(@NotNull TaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(@NotNull Runnable task) {
        SecurityContext context = SecurityContextHolder.getContext();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        executor.execute(() -> {
            SecurityContextHolder.setContext(context);
            RequestContextHolder.setRequestAttributes(requestAttributes);
            try {
                task.run();
            } catch (Exception e) {
                log.error("异步任务执行失败：", e);
                throw new ServiceException(e.getMessage(), 500);
            } finally {
                SecurityContextHolder.clearContext();
                RequestContextHolder.resetRequestAttributes();
            }
        });
    }
}
