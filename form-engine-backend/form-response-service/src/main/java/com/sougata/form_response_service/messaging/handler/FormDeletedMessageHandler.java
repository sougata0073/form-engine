package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormDeletedMessage;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component(MessagingChannelNames.FORM_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormDeletedMessageHandler implements MessageListener {

    private final GenericJacksonJsonRedisSerializer redisSerializer;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), FormDeletedMessage.class);

    }
}
