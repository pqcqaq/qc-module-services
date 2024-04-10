package online.zust.qcqcqc.services.module.log.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.enums.LogLevel;
import online.zust.qcqcqc.services.module.log.mapper.OperatorLogMapper;
import online.zust.qcqcqc.services.module.log.service.LogService;
import online.zust.qcqcqc.services.utils.SpElParser;
import online.zust.qcqcqc.utils.EnhanceService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author qcqcqc
 */
@Service
@Slf4j
public class LogServiceImpl extends EnhanceService<OperatorLogMapper, OperatorLog> implements LogService {
    @Override
    @Async("qc-async")
    public void saveAsync(OperatorLog operatorLog) {
        this.save(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void saveAnnotationLog(final String metadata, final OperationLog operationLog, final Object[] args, final String[] parameterNames, final Object proceed, OperatorLog operatorLog) {
        // 保存日志
        try {
            // 设置 BeanFactoryResolver，用于解析 SpEL 表达式中的 bean
            final HashMap<String, Object> paramMap = new HashMap<>(args.length + 1);
            for (int i = 0; i < args.length; i++) {
                // 把 Controller 方法中的参数都设置到 context 中，使用参数名称作为 key。
                paramMap.put(parameterNames[i], args[i]);
            }
            // 把方法的返回值也设置到 context 中，使用 returnValue 作为 key。
            paramMap.put("returnValue", proceed);

            // 如果不需要记录日志，则直接执行下去，不需要走下面的日志逻辑
            final String condition = operationLog.condition();
            if (!condition.isEmpty()) {
                // 解析注解上定义的表达式，获取到结果
                final Boolean result = SpElParser.parseExpression(condition, paramMap, Boolean.class);
                if (Boolean.FALSE.equals(result)) {
                    return;
                }
            }
            // 解析注解上定义的表达式，获取到结果
            final String result = SpElParser.parseExpression(operationLog.value(), paramMap, String.class);
            operatorLog.setMsg(result);
        } catch (Exception e) {
            log.error("操作日志SpEL表达式解析异常: {}", e.getMessage());
            operatorLog.setMsg("生成日志异常，请联系管理员");
            operatorLog.setCause("解析异常：" + operationLog.value());
        }
        operatorLog.setMetadata(metadata);
        this.saveAsync(operatorLog);
    }

    private String getMsg(String spEl) {
        return SpElParser.parseExpression(spEl, new HashMap<>(0), String.class);
    }

    @Override
    @Async("qc-async")
    public void info(String spEl) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.INFO);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(true);
        operatorLog.setCause("无");
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void error(String spEl) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.ERROR);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(false);
        operatorLog.setCause("无");
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void error(String spEl, Throwable e) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.ERROR);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(false);
        operatorLog.setCause(e.getMessage());
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void error(Throwable e) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.ERROR);
        operatorLog.setMsg(e.getMessage());
        operatorLog.setSuccess(false);
        operatorLog.setCause(e.getCause().getMessage());
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void error(String spEl, String cause) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.ERROR);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(false);
        operatorLog.setCause(cause);
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void debug(String spEl) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.DEBUG);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(true);
        operatorLog.setCause("无");
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void warn(String spEl) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.WARN);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(true);
        operatorLog.setCause("无");
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void warn(String spEl, Throwable e) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.WARN);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(false);
        operatorLog.setCause(e.getMessage());
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void warn(Throwable e) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.WARN);
        operatorLog.setMsg(e.getMessage());
        operatorLog.setSuccess(false);
        operatorLog.setCause(e.getCause().getMessage());
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void warn(String spEl, String cause) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.WARN);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(false);
        operatorLog.setCause(cause);
        this.saveAsync(operatorLog);
    }
}
