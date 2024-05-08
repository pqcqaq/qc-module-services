package online.zust.qcqcqc.services.utils.config;

import online.zust.qcqcqc.services.config.defaults.DefaultCurrentUserGetter;
import online.zust.qcqcqc.services.utils.JwtUtils;
import online.zust.qcqcqc.services.utils.ResponseUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author qcqcqc
 * Date: 2024/5/8
 * Time: 下午4:32
 */
@Configuration
@Import({
        DefaultCurrentUserGetter.class,
        JwtUtils.class,
        ResponseUtils.class,
})
public class MbpUtilsAutoInject {
}
