package com.sougata.form_data_service.messaging.handler;

import com.sougata.form_data_service.repository.FormResponseRepository;
import com.sougata.form_data_service.util.CacheUtil;
import com.sougata.form_engine.constant.cache.FormDataCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormDeletedMessage;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component(MessagingChannelNames.FORM_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormDeletedMessageHandler implements MessageListener {

    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final FormResponseRepository formResponseRepository;
    private final RedissonClient redissonClient;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), FormDeletedMessage.class);

        formResponseRepository.deleteByFormId(messageData.getFormId());
        // TODO: Delete response from each question tables

        var redissonKeys = redissonClient.getKeys();

        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormDataCacheNames.FORM_RESPONSE_NO_PAGE, "formId=" + messageData.getFormId()) + "::*"
        );
    }
}
