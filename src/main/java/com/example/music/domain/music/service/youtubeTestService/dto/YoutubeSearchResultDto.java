package com.example.music.domain.music.service.youtubeTestService.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class YoutubeSearchResultDto {

	private String musicYoutubeId;
	private long musicLength;

	public static YoutubeSearchResultDto of(String musicYoutubeId, long musicLength) {
		return new YoutubeSearchResultDto(musicYoutubeId, musicLength);
	}
}