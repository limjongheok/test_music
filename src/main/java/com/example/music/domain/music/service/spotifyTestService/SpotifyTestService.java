package com.example.music.domain.music.service.spotifyTestService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.music.domain.music.service.spotifyTestService.dto.AccessTokenResponse;
import com.example.music.domain.music.service.spotifyTestService.dto.Image;
import com.example.music.domain.music.service.spotifyTestService.dto.SearchMusic;
import com.example.music.domain.music.service.spotifyTestService.dto.SearchResponse;
import com.example.music.domain.music.service.spotifyTestService.dto.SpotifySearchResultDto;
import com.example.music.domain.music.service.spotifyTestService.dto.Track;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotifyTestService {

	@Value("${spotify.client.id}")
	private String clientId;

	@Value("${spotify.client.secret}")
	private String clientSecret;

	public SpotifySearchResultDto searchSpotifyMusicList(SearchMusic searchMusic) {
		String accessToken = getAccessToken();
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		String searchTrack = searchMusic.getTrack();
		String searchArtist = searchMusic.getArtist();
		String query = searchTrack + " " + searchArtist;  // ✅ 자연어 키워드 기반 검색

		log.info("spotify query : {}", query);

		ResponseEntity<SearchResponse> responseEntityKr = getKrresponseEntity(restTemplate, headers, query);
		ResponseEntity<SearchResponse> responseEntityEn = getEnresponseEntity(restTemplate, headers, query);

		SpotifySearchResultDto music = getMusics(responseEntityKr, responseEntityEn);

		return music;
	}

	private String getAccessToken() {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(clientId, clientSecret);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "client_credentials");

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<AccessTokenResponse> responseEntity = restTemplate.exchange(
			"https://accounts.spotify.com/api/token",
			HttpMethod.POST,
			requestEntity,
			AccessTokenResponse.class);

		return responseEntity.getBody().getAccess_token();
	}

	private ResponseEntity<SearchResponse> getKrresponseEntity(RestTemplate restTemplate, HttpHeaders headers,
		String encodedQuery) {
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		return restTemplate.exchange(
			"https://api.spotify.com/v1/search?q={query}&type=track&limit=1&market=KR&locale=ko-KR",
			HttpMethod.GET,
			requestEntity,
			SearchResponse.class,
			encodedQuery);
	}

	private ResponseEntity<SearchResponse> getEnresponseEntity(RestTemplate restTemplate, HttpHeaders headers,
		String encodedQuery) {
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		return restTemplate.exchange(
			"https://api.spotify.com/v1/search?q={query}&type=track&limit=1&market=KR&",
			HttpMethod.GET,
			requestEntity,
			SearchResponse.class,
			encodedQuery);
	}

	private SpotifySearchResultDto getMusics(ResponseEntity<SearchResponse> responseEntityKr,
		ResponseEntity<SearchResponse> responseEntityEn) {

		SpotifySearchResultDto musicKr = extractFirstMusic(responseEntityKr);
		SpotifySearchResultDto musicEn = extractFirstMusic(responseEntityEn);

		if (musicKr != null) {

			return musicKr;
		}
		if (musicEn != null) {
			log.info("music artist : {}", musicKr.getMusicArtist());

			return musicEn;
		}
		return null;
	}

	private SpotifySearchResultDto extractFirstMusic(ResponseEntity<SearchResponse> response) {
		List<Track> items = response.getBody().getTracks().getItems();
		if (items == null || items.isEmpty())
			return null;

		Track track = items.get(0);
		String imageUrl = null;

		List<Image> images = track.getAlbum().getImages();
		if (images != null && !images.isEmpty()) {
			imageUrl = images.get(0).getUrl();
		}

		return SpotifySearchResultDto.of(track, imageUrl);
	}
}
