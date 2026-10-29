package com.sougata.form_data_service.messaging.handler;

import com.sougata.form_data_service.util.CacheUtil;
import com.sougata.form_engine.constant.cache.CommonCacheNames;
import com.sougata.form_engine.constant.cache.FormDataCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormResponseDeleteMessage;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component(MessagingChannelNames.FORM_RESPONSE_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormResponseDeletedMessageHandler implements MessageListener {

    private final RedissonClient redissonClient;
    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), FormResponseDeleteMessage.class);

        var formResponseNoPageCacheKey = CacheUtil.buildKey(
                FormDataCacheNames.FORM_RESPONSE_NO_PAGE,
                "formId=" + messageData.getFormId(),
                "formResponseId=" + messageData.getFormResponseId()
        );
        var isResponseAlreadySubmittedCacheKey = CacheUtil.buildKey(
                FormDataCacheNames.IS_RESPONSE_ALREADY_SUBMITTED,
                "formId=" + messageData.getFormId(),
                "userId=" + messageData.getUserId()
        );

        var formResponseCountRedisKey = CommonCacheNames.FORM_RESPONSE_COUNT + "::" + messageData.getFormId();

        if (stringRedisTemplate.hasKey(formResponseCountRedisKey)) {
            stringRedisTemplate.opsForValue().decrement(formResponseCountRedisKey);
        }

        var redissonKeys = redissonClient.getKeys();

        redissonKeys.delete(
                formResponseNoPageCacheKey,
                isResponseAlreadySubmittedCacheKey
        );

    }

}
