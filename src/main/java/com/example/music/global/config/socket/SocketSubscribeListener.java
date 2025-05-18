package com.example.music.global.config.socket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketSubscribeListener {

	private final SimpMessagingTemplate template;

	public void pushMessageToClients(String message) {
		template.convertAndSend("/music/message", message); // 모든 구독자에게 전송
	}
}
