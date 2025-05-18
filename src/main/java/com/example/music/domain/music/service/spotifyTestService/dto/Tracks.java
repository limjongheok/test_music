package com.example.music.domain.music.service.spotifyTestService.dto;

import java.util.List;

import com.example.music.domain.music.service.spotifyTestService.dto.Track;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tracks {

	private String href;
	private int limit;
	private String next;
	private int offset;
	private String previous;
	private int total;
	private List<Track> items;
}