package com.example.music.domain.music.service.youtubeTestService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.music.domain.music.service.spotifyTestService.dto.SpotifySearchResultDto;
import com.example.music.domain.music.service.youtubeTestService.dto.YoutubeSearchResultDto;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class YouTubeTestService {

	private final YouTube youtubeApi;
	private final RedisTemplate<String, Object> redisYoutubeTemplate;
	private final ConvertTime convertTime;

	public YoutubeSearchResultDto searchVideo(SpotifySearchResultDto spotifySearchResultDto) {
		String title = spotifySearchResultDto.getMusicTitle();
		String artist = spotifySearchResultDto.getMusicArtist();
		String redisKey = buildRedisKey(title, artist);

		YoutubeSearchResultDto cached = getCachedResult(redisKey);
		if (cached != null)
			return cached;

		try {
			YoutubeSearchResultDto bestResult = findBestYoutubeVideo(title, artist,
				spotifySearchResultDto.getSpotifyMusicDuration());

			if (bestResult.getMusicYoutubeId() != null) {
				cacheResult(redisKey, bestResult);
				return bestResult;
			} else {
				log.warn("not found video");
				return handleNoResult(redisKey);
			}
		} catch (Exception e) {
			log.error("YouTube search error", e);
			return handleNoResult(redisKey);
		}
	}

	private String buildRedisKey(String title, String artist) {
		return "music:" + title + artist;
	}

	private YoutubeSearchResultDto getCachedResult(String key) {
		YoutubeSearchResultDto cached = (YoutubeSearchResultDto)redisYoutubeTemplate.opsForValue().get(key);
		if (cached != null && cached.getMusicYoutubeId() != null) {
			log.info("find cached");
			return cached;
		}
		return null;
	}

	private void cacheResult(String key, YoutubeSearchResultDto result) {
		redisYoutubeTemplate.opsForValue().set(key, result);
	}

	private YoutubeSearchResultDto handleNoResult(String key) {
		YoutubeSearchResultDto emptyResult = YoutubeSearchResultDto.of(null, 0L);
		redisYoutubeTemplate.opsForValue().set(key, emptyResult);
		return null;
	}

	private YoutubeSearchResultDto findBestYoutubeVideo(String title, String artist, long spotifyDuration) throws
		IOException {
		String query = title + " " + artist;

		YouTube.Search.List searchRequest = youtubeApi.search().list(Arrays.asList("id", "snippet"));
		searchRequest.setQ(query);
		searchRequest.setType(Arrays.asList("video"));
		searchRequest.setMaxResults(20L);
		searchRequest.setFields("items(id(videoId),snippet(publishedAt,channelId,title,description))");

		List<SearchResult> searchResults = searchRequest.execute().getItems();

		BigInteger maxViews = BigInteger.ZERO;
		YoutubeSearchResultDto bestResult = YoutubeSearchResultDto.of(null, 0L);

		for (SearchResult searchResult : searchResults) {
			String videoId = searchResult.getId().getVideoId();

			Video video = youtubeApi.videos()
				.list(Arrays.asList("id", "statistics", "contentDetails"))
				.setId(Arrays.asList(videoId))
				.execute()
				.getItems()
				.get(0);

			long playTime = convertTime.convertDurationToMillis(video.getContentDetails().getDuration());

			if (Math.abs(spotifyDuration - playTime) > 5000)
				continue;

			BigInteger viewCount = video.getStatistics().getViewCount();
			if (viewCount.compareTo(maxViews) > 0 && viewCount.compareTo(BigInteger.valueOf(100_000)) > 0) {
				maxViews = viewCount;
				bestResult = YoutubeSearchResultDto.of(videoId, playTime);
			}
		}

		return bestResult;
	}
}