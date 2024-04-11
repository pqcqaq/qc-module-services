package online.zust.qcqcqc.services.module.log.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author qcqcqc
 * Date: 2024/4/11
 * Time: 23:50
 */
@Component
public class RequestMetadata implements MetadataAppender {
    @Override
    public String appendMetadata() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return "NO REQUEST INFO";
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取请求的接口地址
        String requestUri = request.getRequestURI();
        // 获取请求的方法
        String method = request.getMethod();
        // 获取请求的参数
        String queryString = request.getQueryString();
        // 返回请求的接口地址、方法、参数
        return "请求地址：" + requestUri + ",请求方法：" + method + ",请求参数：" + queryString;
    }
}
