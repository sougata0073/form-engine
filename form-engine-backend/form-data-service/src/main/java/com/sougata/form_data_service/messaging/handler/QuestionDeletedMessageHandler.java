package com.sougata.form_data_service.messaging.handler;

import com.sougata.form_data_service.repository.FormResponseRepository;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.QuestionDeleteMessage;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Component(MessagingChannelNames.QUESTION_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class QuestionDeletedMessageHandler implements MessageListener {

    private final ObjectMapper objectMapper;
    private final FormResponseRepository formResponseRepository;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {

        var messageData = objectMapper.readValue(
                new String(message.getBody(), StandardCharsets.UTF_8), QuestionDeleteMessage.class
        );
    }
}
