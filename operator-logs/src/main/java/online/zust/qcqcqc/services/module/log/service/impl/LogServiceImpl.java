package online.zust.qcqcqc.services.module.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.log.annotation.OperationLog;
import online.zust.qcqcqc.services.module.log.common.MetadataAppender;
import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.enums.LogLevel;
import online.zust.qcqcqc.services.module.log.mapper.OperatorLogMapper;
import online.zust.qcqcqc.services.module.log.service.LogService;
import online.zust.qcqcqc.services.utils.SpElParser;
import online.zust.qcqcqc.utils.EnhanceService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qcqcqc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LogServiceImpl extends EnhanceService<OperatorLogMapper, OperatorLog> implements LogService {

    private final List<MetadataAppender> metadataAppenders;
    private final ObjectMapper objectMapper;

    @Override
    @Async("qc-async")
    public void saveAsync(OperatorLog operatorLog) {
        this.save(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void saveAnnotationLog(final OperationLog operationLog, final Object[] args, final String[] parameterNames, final Object proceed, OperatorLog operatorLog) {
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
        operatorLog.setMetadata(getMetadata());
        this.saveAsync(operatorLog);
    }

    private String getMsg(String spEl) {
        return SpElParser.parseExpression(spEl, new HashMap<>(0), String.class);
    }

    public String getMetadata() {
        Map<String, String> metadataMap = new HashMap<>(metadataAppenders.size());
        for (MetadataAppender metadataAppender : metadataAppenders) {
            metadataMap.put(metadataAppender.getKey(), metadataAppender.appendMetadata());
        }
        try {
            return objectMapper.writeValueAsString(metadataMap);
        } catch (JsonProcessingException e) {
            log.error("Metadata解析异常: {}", e.getMessage());
            return "{}";
        }
    }

    @Override
    @Async("qc-async")
    public void info(String spEl) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.INFO);
        operatorLog.setMsg(getMsg(spEl));
        operatorLog.setSuccess(true);
        operatorLog.setCause("无");
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
        this.saveAsync(operatorLog);
    }

    @Override
    @Async("qc-async")
    public void error(Throwable e) {
        OperatorLog operatorLog = new OperatorLog();
        operatorLog.setLevel(LogLevel.ERROR);
        operatorLog.setMsg(e.getMessage());
        operatorLog.setSuccess(false);
        Throwable cause = e.getCause();
        if (cause == null) {
            cause = e;
        }
        operatorLog.setCause(cause.getMessage());
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
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
        operatorLog.setMetadata(getMetadata());
        this.saveAsync(operatorLog);
    }

    @Override
    public List<String> getMetadataKeys() {
        return metadataAppenders.stream().map(MetadataAppender::getKey).toList();
    }

    @Override
    public QueryWrapper<OperatorLog> getMetadataFuzzyQueryWrapper(String key, String value) {
        QueryWrapper<OperatorLog> operatorLogQueryWrapper = new QueryWrapper<>();
        // 利用mysql8的json数据格式匹配
        operatorLogQueryWrapper.last("AND metadata ->> '$." + key + "' LIKE '%" + value + "%'");
        return operatorLogQueryWrapper;
    }
}
