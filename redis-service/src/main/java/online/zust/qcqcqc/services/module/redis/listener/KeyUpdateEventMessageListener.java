package online.zust.qcqcqc.services.module.redis.listener;

import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.listener.KeyspaceEventMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

/**
 * @author qcqcqc
 */
public class KeyUpdateEventMessageListener extends KeyspaceEventMessageListener implements ApplicationEventPublisherAware {
    private static final Topic KEYEVENT_DELETE_TOPIC = new PatternTopic("__keyevent@*__:set");
    @Nullable
    private ApplicationEventPublisher publisher;

    public KeyUpdateEventMessageListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doRegister(RedisMessageListenerContainer listenerContainer) {
        listenerContainer.addMessageListener(this, KEYEVENT_DELETE_TOPIC);
    }

    @Override
    protected void doHandleMessage(Message message) {
        this.publishEvent(new RedisKeyExpiredEvent(message.getBody()));
    }

    protected void publishEvent(RedisKeyExpiredEvent event) {
        if (this.publisher != null) {
            this.publisher.publishEvent(event);
        }

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
