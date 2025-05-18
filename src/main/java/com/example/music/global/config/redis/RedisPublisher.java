package com.example.music.global.config.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

	private final RedisTemplate<String, Object> template;

	public void publish(ChannelTopic topic, MessageDto dto) {
		template.convertAndSend(topic.getTopic(), dto);
	}
}