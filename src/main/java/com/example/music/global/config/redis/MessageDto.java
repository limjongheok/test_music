package com.example.music.global.config.redis;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MessageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private String sender;
	private String roomId;
}