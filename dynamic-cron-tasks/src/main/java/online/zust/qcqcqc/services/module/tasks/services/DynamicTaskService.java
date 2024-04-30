package online.zust.qcqcqc.services.module.tasks.services;

import online.zust.qcqcqc.services.module.tasks.entity.DynamicCronTask;
import online.zust.qcqcqc.utils.IServiceEnhance;
import org.springframework.scheduling.config.CronTask;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午8:51
 */
public interface DynamicTaskService extends IServiceEnhance<DynamicCronTask> {
    /**
     * 根据cronKey查询任务
     *
     * @return DynamicCronTask
     */
    List<DynamicCronTask> fineAllBootTask();

    /**
     * 根据id获取正在运行任务
     *
     * @param id 任务id
     * @return ScheduledFuture<?>
     */
    ScheduledFuture<?> getScheduledFutureById(Long id);

    /**
     * 添加任务
     *
     * @param id       任务id
     * @param schedule 任务
     */
    void addScheduledTask(Long id, ScheduledFuture<?> schedule);

    /**
     * 开启任务
     *
     * @param task 任务
     */
    void startTask(DynamicCronTask task);

    /**
     * 开启任务
     *
     * @param id 任务id
     */
    void startTask(Long id);

    /**
     * 停止任务
     *
     * @param id 任务id
     * @return boolean
     */
    void stopTask(Long id);

    /**
     * 设置任务启动状态
     *
     * @param id    任务id
     * @param start 启动状态
     */
    void setRunning(Long id, boolean start);

    /**
     * 设置任务成功状态
     *
     * @param id      任务id
     * @param success 成功状态
     */
    void setSuccess(Long id, boolean success);

    /**
     * 获取Spel任务元数据
     *
     * @param task 任务
     * @return String
     */
    String getSpelTaskMetadata(DynamicCronTask task);

    /**
     * 开启在服务启动时执行的任务
     */
    void startOnBootTasks();

    /**
     * 修改任务cron表达式
     *
     * @param id             任务id
     * @param cronExpression cron表达式
     */
    void changeTaskCronExpression(Long id, String cronExpression);

    /**
     * 获取所有任务
     *
     * @return List<CronTask>
     */
    List<ScheduledFuture<?>> getCronTaskList();
}
