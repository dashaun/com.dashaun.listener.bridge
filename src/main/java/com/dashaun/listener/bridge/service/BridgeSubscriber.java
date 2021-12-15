package com.dashaun.listener.bridge.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BridgeSubscriber {

    @Value("${redis.topic.name:mqtt-channel}")
    private String topic;

    @Bean
    ReactiveRedisMessageListenerContainer container(ReactiveRedisConnectionFactory connectionFactory) {

        ReactiveRedisMessageListenerContainer container = new ReactiveRedisMessageListenerContainer(connectionFactory);
        container.receive(ChannelTopic.of(topic))
                .map(ReactiveSubscription.Message::getMessage)
                .switchIfEmpty(Mono.error(new IllegalArgumentException()))
                .subscribe(c-> log.info("Message:" + c));
        return container;

    }

}
