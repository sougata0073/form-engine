package com.sougata.form_service.messaging.handler;

import com.sougata.form_engine.constant.DistributedLockNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.UserCreatedMessage;
import com.sougata.form_service.model.formSchema.FormDefaultSettings;
import com.sougata.form_service.repository.formSchema.FormDefaultSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component(MessagingChannelNames.USER_CREATED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class UserCreatedMessageHandler implements MessageListener {

    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final FormDefaultSettingsRepository formDefaultSettingsRepository;
    private final RedissonClient redissonClient;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), UserCreatedMessage.class);

        var lockKey = DistributedLockNames.formDefaultSettingsSave(messageData.getUserId());
        var rLock = redissonClient.getLock(lockKey);

        rLock.lock();

        try {
            if (formDefaultSettingsRepository.existsById(messageData.getUserId())) {
                return;
            }

            var formDefaultSettings = new FormDefaultSettings();
            formDefaultSettings.setUserId(messageData.getUserId());

            formDefaultSettingsRepository.save(formDefaultSettings);

        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }
}
