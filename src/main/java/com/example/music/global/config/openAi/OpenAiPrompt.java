package com.example.music.global.config.openAi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum OpenAiPrompt {

	RECOMMEND_MUSIC("""
		Recommend 5 songs that match the emotion: "{emotion}". 
		Only return the music list — no explanations or extra text. 
		Respond in this JSON format: 
		{ "songs": [ { "track": "string", "artist": "string" }, ... ] }
		""");

	private String text;
}
