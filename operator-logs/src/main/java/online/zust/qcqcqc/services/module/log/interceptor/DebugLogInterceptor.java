package online.zust.qcqcqc.services.module.log.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import online.zust.qcqcqc.services.module.log.entity.OperatorLog;
import online.zust.qcqcqc.services.module.log.enums.LogLevel;
import online.zust.qcqcqc.services.module.log.utils.SystemLogger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qcqcqc
 * Date: 2024/4/12
 * Time: 23:47
 */
@Component
@RequiredArgsConstructor
public class DebugLogInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    private static final ThreadLocal<Map<String, String>> operatorLogMetadataThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 获取请求处理的方法
        String methodName;
        if (handler instanceof HandlerMethod handlerMethod) {
            methodName = handlerMethod.getMethod().getName();
        } else {
            methodName = handler.toString();
        }
        HashMap<String, String> stringStringHashMap = new HashMap<>(3);
        stringStringHashMap.put("preHandle", "handler:" + methodName);
        operatorLogMetadataThreadLocal.set(stringStringHashMap);
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        Map<String, String> stringStringMap = operatorLogMetadataThreadLocal.get();
        stringStringMap.put("postHandle", "modelAndView:" + modelAndView);
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        OperatorLog operatorLog = new OperatorLog();
        Map<String, String> stringStringMap = operatorLogMetadataThreadLocal.get();
        stringStringMap.put("afterCompletion", ex == null ? "success" : "error:" + ex.getMessage());
        operatorLog.setMsg("[DEBUG-LOG]--->" + request.getRequestURI() + " : " + request.getMethod() + " : " + request.getRemoteAddr() + " : " + request.getRemotePort() + " : " + request.getRemoteUser() + " : " + request.getRemoteHost());
        // 将map转为json
        operatorLog.setMetadata(objectMapper.writeValueAsString(stringStringMap));
        operatorLog.setLevel(LogLevel.DEBUG);
        operatorLog.setSuccess(ex == null);
        operatorLog.setCause(ex == null ? "NULL" : ex.getMessage());
        SystemLogger.saveLog(operatorLog);
        operatorLogMetadataThreadLocal.remove();
    }
}
