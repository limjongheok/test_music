package com.example.music.domain.music.service.youtubeTestService;

import org.springframework.stereotype.Component;

@Component
public class ConvertTime {

	public long convertDurationToMillis(String duration) {
		java.time.Duration parsedDuration = java.time.Duration.parse(duration);
		return parsedDuration.toMillis();
	}
}
