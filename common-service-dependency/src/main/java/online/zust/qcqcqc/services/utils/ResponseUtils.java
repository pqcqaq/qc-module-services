package online.zust.qcqcqc.services.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author qcqcqc
 * Date: 2024/3/18
 * Time: 9:01
 */
@Component
@Slf4j
public class ResponseUtils {

    private static ObjectMapper objectMapper;

    public ResponseUtils(ObjectMapper objectMapper) {
        ResponseUtils.objectMapper = objectMapper;
    }

    public static <T> void renderString(HttpServletResponse response, T data) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().println(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
