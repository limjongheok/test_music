package com.example.music.domain.music.service.openAiTestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.music.domain.music.service.spotifyTestService.dto.SearchMusic;
import com.example.music.domain.music.service.spotifyTestService.SpotifyTestService;
import com.example.music.domain.music.service.spotifyTestService.dto.SpotifySearchResultDto;
import com.example.music.domain.music.service.youtubeTestService.YouTubeTestService;
import com.example.music.domain.music.service.youtubeTestService.dto.YoutubeSearchResultDto;
import com.example.music.global.config.openAi.ChatGPTRequest;
import com.example.music.global.config.openAi.ChatGPTResponse;
import com.example.music.global.config.openAi.OpenAiPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OpenAiTestController {

	@Value("${openai.api.url}")
	private String apiURL;

	private final RestTemplate openAiTemplate;
	private final ObjectMapper mapper;
	private final SpotifyTestService spotifyTestService;
	private final YouTubeTestService youTubeTestService;

	@GetMapping("/chat")
	public String chat(@RequestParam(name = "prompt") String prompt) throws JsonProcessingException {
		String finalPrompt = OpenAiPrompt.RECOMMEND_MUSIC.getText().replace("{emotion}", prompt);
		ChatGPTRequest request = ChatGPTRequest.of(finalPrompt);
		ChatGPTResponse chatGPTResponse = openAiTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

		String content = chatGPTResponse.getChoices().get(0).getMessage().getContent();
		JsonNode root = mapper.readTree(content);
		JsonNode songs = root.get("songs");

		List<SearchMusic> recommendMusics = mapper.readerForListOf(SearchMusic.class).readValue(songs.toString());

		for(SearchMusic searchMusic : recommendMusics){
			log.info("searchMusic ai : {}" , searchMusic.getTrack() + " " +searchMusic.getArtist());
			SpotifySearchResultDto spotifySearchResultDto = spotifyTestService.searchSpotifyMusicList(searchMusic);
			log.info("title and artist : {}" +  spotifySearchResultDto.getMusicTitle() + " " +spotifySearchResultDto.getMusicArtist());
			YoutubeSearchResultDto youtubeSearchResultDto = youTubeTestService.searchVideo(spotifySearchResultDto);
			log.info("url : {}" , youtubeSearchResultDto.getMusicYoutubeId()+ " "+" " + youtubeSearchResultDto.getMusicLength() + spotifySearchResultDto.getMusicTitle() + " " +spotifySearchResultDto.getMusicArtist());
		}

		return chatGPTResponse.getChoices().get(0).getMessage().getContent();
	}
}