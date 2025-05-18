package com.example.music.global.config.openAi;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatGPTRequest {

	private String model;
	private List<Message> messages;

	public static ChatGPTRequest of(String prompt) {
		List<Message> messages = new ArrayList<>();
		messages.add(new Message("user", prompt));
		return new ChatGPTRequest("gpt-4o-mini", messages);
	}
}
