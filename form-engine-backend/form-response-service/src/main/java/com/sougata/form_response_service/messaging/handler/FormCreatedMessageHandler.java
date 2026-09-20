package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormCreatedMessage;
import com.sougata.form_response_service.model.FormResponseSummary;
import com.sougata.form_response_service.repository.FormResponseRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component(MessagingChannelNames.FORM_CREATED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormCreatedMessageHandler implements MessageListener {

    private final FormResponseRepository formResponseRepository;
    private final GenericJacksonJsonRedisSerializer redisSerializer;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), FormCreatedMessage.class);

        var formResponseCount = new FormResponseSummary();

        formResponseCount.setFormId(messageData.getFormId());
        formResponseCount.setResponseCount(0L);

        formResponseRepository.save(formResponseCount);
    }
}
