package online.zust.qcqcqc.services.module.redis.listener.interfaces;

import org.intellij.lang.annotations.Language;
import org.springframework.data.redis.connection.Message;

/**
 * @author qcqcqc
 * Date: 2024/4/13
 * Time: 20:36
 */
public interface KeyExpiredObserver {
    /**
     * 需要监听的key
     *
     * @return key
     */
    @Language("RegExp")
    default String listenerKey() {
        return null;
    }

    /**
     * 过期key监听
     *
     * @param message 消息
     * @param pattern 模式
     */
    void onMessage(Message message, byte[] pattern);
}
