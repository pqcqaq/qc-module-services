package online.zust.qcqcqc.services.module.tasks;

import online.zust.qcqcqc.services.module.log.utils.SystemLogger;
import online.zust.qcqcqc.services.module.tasks.entity.DynamicCronTask;
import online.zust.qcqcqc.services.module.tasks.exception.DynamicTaskException;
import online.zust.qcqcqc.services.module.tasks.services.DynamicTaskService;
import online.zust.qcqcqc.services.utils.SpElParser;
import online.zust.qcqcqc.utils.utils.ProxyUtil;

import java.util.HashMap;

/**
 * @author qcqcqc
 * Date: 2024/4/25
 * Time: 下午9:22
 */
public class RunTaskGenerator {

    private static DynamicTaskService dynamicTaskService;

    /**
     * 获取DynamicTaskService
     *
     * @return DynamicTaskService
     */
    private static DynamicTaskService getDynamicTaskService() {
        if (dynamicTaskService == null) {
            dynamicTaskService = ProxyUtil.getBean(DynamicTaskService.class);
        }
        return dynamicTaskService;
    }

    /**
     * 解析任务
     *
     * @param task 任务
     * @return Runnable
     */
    public static Runnable parseTask(DynamicCronTask task) {
        switch (task.getTaskType()) {
            case FETCH -> {
                return parseApiTask(task);
            }
            case SPEL -> {
                return parseSpelTask(task);
            }
            case SHELL -> {
                return parseShellTask(task);
            }
            case PYTHON -> {
                return parsePythonTask(task);
            }
            case JAVASCRIPT -> {
                return parseJavaScriptTask(task);
            }
            case GROOVY -> {
                return parseGroovyTask(task);
            }
            default ->
                    throw new DynamicTaskException("Unexpected value: " + task.getTaskType() + " in task:id=" + task.getId(), 500);
        }
    }

    /**
     * 解析Groovy任务
     *
     * @param task 任务
     * @return Runnable
     */
    private static Runnable parseGroovyTask(DynamicCronTask task) {
        return () -> {
            getDynamicTaskService().setRunning(task.getId(), true);
            try {
                SystemLogger.info("'Groovy脚本任务开始执行:" + task.getTaskName() + "'");
                // TODO: 2024/4/25 执行Groovy脚本
                getDynamicTaskService().setSuccess(task.getId(), true);
            } catch (Exception e) {
                SystemLogger.error("'Groovy脚本任务执行失败:" + task.getTaskName() + "'", e);
                getDynamicTaskService().setSuccess(task.getId(), false);
            } finally {
                getDynamicTaskService().setRunning(task.getId(), false);
            }
        };
    }

    /**
     * 解析JavaScript任务
     *
     * @param task 任务
     * @return Runnable
     */
    private static Runnable parseJavaScriptTask(DynamicCronTask task) {
        return () -> {
            getDynamicTaskService().setRunning(task.getId(), true);
            try {
                SystemLogger.info("'JavaScript脚本任务开始执行:" + task.getTaskName() + "'");
                // TODO: 2024/4/25 执行JavaScript脚本
                getDynamicTaskService().setSuccess(task.getId(), true);
            } catch (Exception e) {
                SystemLogger.error("'JavaScript脚本任务执行失败:" + task.getTaskName() + "'", e);
                getDynamicTaskService().setSuccess(task.getId(), false);
            } finally {
                getDynamicTaskService().setRunning(task.getId(), false);
            }
        };
    }

    /**
     * 解析Python任务
     *
     * @param task 任务
     * @return Runnable
     */
    private static Runnable parsePythonTask(DynamicCronTask task) {
        return () -> {
            getDynamicTaskService().setRunning(task.getId(), true);
            try {
                SystemLogger.info("'Python脚本任务开始执行:" + task.getTaskName() + "'");
                // TODO: 2024/4/25 执行Python脚本
                getDynamicTaskService().setSuccess(task.getId(), true);
            } catch (Exception e) {
                SystemLogger.error("'Python脚本任务执行失败:" + task.getTaskName() + "'", e);
                getDynamicTaskService().setSuccess(task.getId(), false);
            } finally {
                getDynamicTaskService().setRunning(task.getId(), false);
            }
        };
    }

    /**
     * 解析Shell任务
     *
     * @param task 任务
     * @return Runnable
     */
    private static Runnable parseShellTask(DynamicCronTask task) {
        return () -> {
            getDynamicTaskService().setRunning(task.getId(), true);
            try {
                SystemLogger.info("'Shell脚本任务开始执行:" + task.getTaskName() + "'");
                // TODO: 2024/4/25 执行Shell脚本
                getDynamicTaskService().setSuccess(task.getId(), true);
            } catch (Exception e) {
                SystemLogger.error("'Shell脚本任务执行失败:" + task.getTaskName() + "'", e);
                getDynamicTaskService().setSuccess(task.getId(), false);
            } finally {
                getDynamicTaskService().setRunning(task.getId(), false);
            }
        };
    }

    /**
     * 解析Spel任务
     *
     * @param task 任务
     * @return Runnable
     */
    private static Runnable parseSpelTask(DynamicCronTask task) {
        String spelTaskMetadata = getDynamicTaskService().getSpelTaskMetadata(task);
        return () -> {
            getDynamicTaskService().setRunning(task.getId(), true);
            try {
                SystemLogger.info("'Spel表达式任务开始执行:" + task.getTaskName() + "'");
                SpElParser.parseExpression(spelTaskMetadata, new HashMap<>(0), String.class);
                getDynamicTaskService().setSuccess(task.getId(), true);
            } catch (Exception e) {
                SystemLogger.error("'Spel表达式任务执行失败:" + task.getTaskName() + "'", e);
                getDynamicTaskService().setSuccess(task.getId(), false);
            } finally {
                getDynamicTaskService().setRunning(task.getId(), false);
            }
        };
    }

    /**
     * 解析Api任务
     *
     * @param task 任务
     * @return Runnable
     */
    private static Runnable parseApiTask(DynamicCronTask task) {
        return () -> {
            try {
                getDynamicTaskService().setRunning(task.getId(), true);
                SystemLogger.info("'Api调用任务开始执行:" + task.getTaskName() + "'");
                // TODO: 2024/4/25 调用Api
                getDynamicTaskService().setSuccess(task.getId(), true);
            } catch (Exception e) {
                SystemLogger.error("'Api调用任务执行失败:" + task.getTaskName() + "'", e);
                getDynamicTaskService().setSuccess(task.getId(), false);
            } finally {
                getDynamicTaskService().setRunning(task.getId(), false);
            }
        };
    }
}
