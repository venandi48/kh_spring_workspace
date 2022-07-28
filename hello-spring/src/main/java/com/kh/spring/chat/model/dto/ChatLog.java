package com.kh.spring.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatLog {

	private int no;
	private String chatroomId;
	private String memberId;
	private String msg;
	private long time;
	
}
