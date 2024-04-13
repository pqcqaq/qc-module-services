package online.zust.qcqcqc.services.module.redis.listener;

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
            listeners.forEach(listener -> listener.onMessage(message, pattern));
        }
    }
}
