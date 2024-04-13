package online.zust.qcqcqc.services.module.redis.listener;

import lombok.extern.slf4j.Slf4j;
import online.zust.qcqcqc.services.module.redis.listener.interfaces.KeyUpdateListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qcqcqc
 */
@Component
@Slf4j
public class QcKeyUpdatePublisher extends KeyUpdateEventMessageListener {

    private List<KeyUpdateListener> listeners;

    @Autowired(required = false)
    public void setListeners(List<KeyUpdateListener> listeners) {
        this.listeners = listeners;
    }

    public QcKeyUpdatePublisher(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        if (listeners != null) {
            listeners.forEach(listener -> {
                try {
                    if (listener.listenerKey() != null) {
                        // 正则测试
                        if (!new String(message.getBody()).matches(listener.listenerKey())) {
                            return;
                        }
                        listener.onMessage(message, pattern);
                        return;
                    }
                    // 如果没有正则表达式，就直接运行
                    listener.onMessage(message, pattern);
                } catch (Exception e) {
                    log.error("KeyUpdateListener error", e);
                }
            });
        }
    }
}
