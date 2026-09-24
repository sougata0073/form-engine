package com.sougata.form_response_service.messaging.handler;

import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.QuestionCreatedMessage;
import com.sougata.form_response_service.service.responseManager.ResponseManagerFactory;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component(MessagingChannelNames.QUESTION_CREATED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class QuestionCreatedMessageHandler implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final ResponseManagerFactory responseManagerFactory;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), QuestionCreatedMessage.class);

        var questionResponseManager = responseManagerFactory.get(messageData.getQuestionDetails().getQuestionType());
    }

}
