package com.example.music.domain.music.service.spotifyTestService.dto;

import java.util.List;

import com.example.music.domain.music.service.spotifyTestService.dto.Album;
import com.example.music.domain.music.service.spotifyTestService.dto.Artist;
import com.example.music.domain.music.service.spotifyTestService.dto.ExternalIds;
import com.example.music.domain.music.service.spotifyTestService.dto.ExternalUrls;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Track {

	private Album album;
	private List<Artist> artists;
	private int disc_number;
	private int duration_ms;
	private boolean explicit;
	private ExternalIds external_ids;
	private ExternalUrls external_urls;
	private String href;
	private String id;
	private boolean is_playable;
	private String name;
	private int popularity;
	private String preview_url;
	private int track_number;
	private String type;
	private String uri;
	private boolean is_local;
}