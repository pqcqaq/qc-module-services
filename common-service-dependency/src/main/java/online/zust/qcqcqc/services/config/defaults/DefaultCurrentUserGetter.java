package online.zust.qcqcqc.services.config.defaults;

import online.zust.qcqcqc.services.utils.CurrentUserGetter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author qcqcqc
 * @date 2024/04
 * @time 10-05-34
 */
@Component
@ConditionalOnMissingBean(CurrentUserGetter.class)
public class DefaultCurrentUserGetter implements CurrentUserGetter {
    @Override
    public Long getCurrentUser() {
        return System.currentTimeMillis();
    }
}
