package online.zust.qcqcqc.services.module.tasks.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.tasks.RunTaskGenerator;
import online.zust.qcqcqc.services.module.tasks.entity.DynamicCronTask;
import online.zust.qcqcqc.services.module.tasks.entity.metadata.SpelMetadata;
import online.zust.qcqcqc.services.module.tasks.enums.TaskStatus;
import online.zust.qcqcqc.services.module.tasks.exception.DynamicTaskException;
import online.zust.qcqcqc.services.module.tasks.mapper.DynamicTaskMapper;
import online.zust.qcqcqc.services.module.tasks.services.DynamicTaskService;
import online.zust.qcqcqc.utils.EnhanceService;
import online.zust.qcqcqc.utils.utils.BeanConvertUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午8:52
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicTaskServiceImpl extends EnhanceService<DynamicTaskMapper, DynamicCronTask> implements DynamicTaskService, DisposableBean {
    private static final ConcurrentHashMap<Long, ScheduledFuture<?>> FUTURE_MAP = new ConcurrentHashMap<>();
    private final ThreadPoolTaskScheduler taskScheduler;

    private void setAllTaskStatusNormal() {
        LambdaQueryWrapper<DynamicCronTask> eq = new LambdaQueryWrapper<DynamicCronTask>().eq(DynamicCronTask::getStatus, TaskStatus.RUNNING);
        List<DynamicCronTask> list = this.list(eq);
        list.forEach(task -> {
            task.setStatus(TaskStatus.NORMAL);
            updateById(task);
        });
    }

    @Override
    public ScheduledFuture<?> getScheduledFutureById(Long id) {
        return FUTURE_MAP.get(id);
    }

    @Override
    public List<DynamicCronTask> fineAllBootTask() {
        LambdaQueryWrapper<DynamicCronTask> eq = new LambdaQueryWrapper<DynamicCronTask>().eq(DynamicCronTask::getOnBoot, true);
        return this.list(eq);
    }

    @Override
    public void addScheduledTask(Long id, ScheduledFuture<?> schedule) {
        // 如果已经存在，就把原来的任务停止，再添加新的任务
        ScheduledFuture<?> scheduledFuture = FUTURE_MAP.get(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        FUTURE_MAP.put(id, schedule);
    }

    @Override
    public void startTask(DynamicCronTask task) {
        ScheduledFuture<?> schedule = taskScheduler.schedule(RunTaskGenerator.parseTask(task), triggerContext -> new CronTrigger(task.getCronExpression()).nextExecution(triggerContext));
        log.info("任务 {} 启动成功", task.getTaskName());
        if (schedule != null) {
            addScheduledTask(task.getId(), schedule);
        }
        // 更新状态
        task.setStatus(TaskStatus.WAITING);
        updateById(task);
    }

    @Override
    public void startTask(Long id) {
        DynamicCronTask task = getById(id);
        if (task == null) {
            throw new DynamicTaskException("任务不存在");
        }
        startTask(task);
    }

    @Override
    public void stopTask(Long id) {
        ScheduledFuture<?> scheduledFuture = FUTURE_MAP.get(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            FUTURE_MAP.remove(id);
            // 更新状态
            LambdaUpdateWrapper<DynamicCronTask> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(DynamicCronTask::getId, id).set(DynamicCronTask::getStatus, TaskStatus.NORMAL);
            update(updateWrapper);
            return;
        }
        throw new DynamicTaskException("任务不存在");
    }

    @Override
    public void setRunning(Long id, boolean start) {
        DynamicCronTask task = getById(id);
        if (task == null) {
            throw new DynamicTaskException("任务不存在");
        }
        task.setStatus(start ? TaskStatus.RUNNING : TaskStatus.WAITING);
        updateById(task);
    }

    @Override
    public void setSuccess(Long id, boolean success) {
        DynamicCronTask task = getById(id);
        if (task == null) {
            throw new DynamicTaskException("任务不存在");
        }
        task.setIsLastSuccess(success);
        updateById(task);
    }

    @Override
    public String getSpelTaskMetadata(DynamicCronTask task) {
        String metadata = task.getMetadata();
        SpelMetadata spelMetadata = BeanConvertUtils.objectConvent(metadata, SpelMetadata.class);
        return spelMetadata.getSpel();
    }

    @Override
    public void startOnBootTasks() {
        fineAllBootTask().forEach(this::startTask);
    }

    @Override
    public void changeTaskCronExpression(Long id, String cronExpression) {
        DynamicCronTask task = getById(id);
        if (task == null) {
            throw new DynamicTaskException("任务不存在");
        }
        task.setCronExpression(cronExpression);
        updateById(task);
        ScheduledFuture<?> scheduledFuture = FUTURE_MAP.get(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            ScheduledFuture<?> schedule = taskScheduler.schedule(RunTaskGenerator.parseTask(task), triggerContext -> new CronTrigger(task.getCronExpression()).nextExecution(triggerContext));
            addScheduledTask(id, schedule);
        }
    }

    @Override
    public List<ScheduledFuture<?>> getCronTaskList() {
        return List.copyOf(FUTURE_MAP.values());
    }

    @Override
    public void destroy() {
        FUTURE_MAP.forEach((id, future) -> {
            if (future != null) {
                future.cancel(true);
            }
        });
    }

    @Component
    @Slf4j
    public static class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
            ApplicationContext applicationContext = event.getApplicationContext();
            DynamicTaskServiceImpl dynamicTaskService = applicationContext.getBean(DynamicTaskServiceImpl.class);
            dynamicTaskService.setAllTaskStatusNormal();
            dynamicTaskService.startOnBootTasks();
        }
    }
}
