package online.zust.qcqcqc.services.module.redis.listener;

import online.zust.qcqcqc.services.module.redis.listener.interfaces.KeyDeleteListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qcqcqc
 * Date: 2024/4/13
 * Time: 20:35
 */
@Component
public class QcKeyDeletePublisher extends KeyDeleteEventMessageListener {

    private List<KeyDeleteListener> listeners;

    @Autowired(required = false)
    public void setListeners(List<KeyDeleteListener> listeners) {
        this.listeners = listeners;
    }

    public QcKeyDeletePublisher(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        if (listeners != null) {
            listeners.forEach(listener -> listener.onMessage(message, pattern));
        }
    }
}
