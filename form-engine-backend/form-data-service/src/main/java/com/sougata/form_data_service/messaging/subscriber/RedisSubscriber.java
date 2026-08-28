package com.sougata.form_data_service.messaging.subscriber;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSubscriber {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        var messageHandlerMap = applicationContext.getBeansOfType(MessageListener.class);

        messageHandlerMap.forEach((handlerName, handler) -> {
            try {
                var channelName = handlerName.split("_")[0];
                redisMessageListenerContainer.addMessageListener(
                        handler, new ChannelTopic(channelName)
                );
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Invalid message handler name");
            }
        });
    }

}
