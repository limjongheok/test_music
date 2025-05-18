package com.example.music.global.config.openAi;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatGPTResponse {

	private List<Choice> choices;

	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Getter
	public static class Choice {
		private int index;
		private Message message;

	}
}
