package com.sougata.form_data_service.messaging.handler;

import com.sougata.form_data_service.repository.FormResponseRepository;
import com.sougata.form_data_service.repository.FormResponseUserRepository;
import com.sougata.form_data_service.service.responseManager.ResponseManagerFactory;
import com.sougata.form_data_service.util.CacheUtil;
import com.sougata.form_engine.constant.cache.FormDataCacheNames;
import com.sougata.form_engine.constant.messaging.CommonMessagingNames;
import com.sougata.form_engine.constant.messaging.MessagingChannelNames;
import com.sougata.form_engine.dto.messaging.FormDeletedMessage;
import com.sougata.form_engine.dto.question.summary.QuestionSummaryDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component(MessagingChannelNames.FORM_DELETED + "_" + CommonMessagingNames.MESSAGE_HANDLER_SUFFIX)
@RequiredArgsConstructor
public class FormDeletedMessageHandler implements MessageListener {

    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final FormResponseRepository formResponseRepository;
    private final FormResponseUserRepository formResponseUserRepository;
    private final RedissonClient redissonClient;
    private final ResponseManagerFactory responseManagerFactory;

    @Override
    public void onMessage(Message message, byte @Nullable [] pattern) {
        var messageData = redisSerializer.deserialize(message.getBody(), FormDeletedMessage.class);

        formResponseRepository.deleteAllByFormId(messageData.getFormId());
        formResponseUserRepository.deleteAllByFormId(messageData.getFormId());

        var questionResponseDeleteFutures = new ArrayList<CompletableFuture<Void>>();

        messageData.getQuestionSummaries()
                .stream().collect(Collectors.groupingBy(QuestionSummaryDto::getQuestionType))
                .forEach((questionType, questionSummaries) -> {
                    var manager = responseManagerFactory.get(questionType);

                    var future = manager.deleteAllByFormId(messageData.getFormId());

                    questionResponseDeleteFutures.add(future);
                });

        CompletableFuture.allOf(questionResponseDeleteFutures.toArray(CompletableFuture[]::new)).join();

        var redissonKeys = redissonClient.getKeys();

        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormDataCacheNames.FORM_RESPONSE_NO_PAGE, "formId=" + messageData.getFormId()) + "::*"
        );
        redissonKeys.deleteByPattern(
                CacheUtil.buildKey(FormDataCacheNames.IS_RESPONSE_ALREADY_SUBMITTED, "formId=" + messageData.getFormId()) + "::*"
        );
    }
}
