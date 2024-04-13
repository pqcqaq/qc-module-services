package online.zust.qcqcqc.services.module.redis.listener;

import online.zust.qcqcqc.services.module.redis.listener.interfaces.KeyExpiredListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qcqcqc
 */
@Component
public class QcKeyExpiredPublisher extends KeyExpirationEventMessageListener {

    private List<KeyExpiredListener> listeners;

    @Autowired(required = false)
    public void setListeners(List<KeyExpiredListener> listeners) {
        this.listeners = listeners;
    }

    public QcKeyExpiredPublisher(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        if (listeners != null) {
            listeners.forEach(listener -> listener.onMessage(message, pattern));
        }
    }
}
