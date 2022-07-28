package com.kh.spring.chat.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.chat.model.dto.ChatLog;
import com.kh.spring.chat.model.dto.ChatMember;
import com.kh.spring.chat.model.service.ChatService;
import com.kh.spring.member.model.dto.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	ChatService chatService;

	@GetMapping("/chat.do")
	public void chat(@AuthenticationPrincipal Member member, Model model) {
		String memberId = member.getMemberId();
		log.debug("memberId = {}", memberId);
		
		ChatMember chatMember = chatService.findChatMemberByMemberId(memberId);
		log.debug("chatMember = {}", chatMember);

		String chatroomId = null;
		if (chatMember != null) {
			// 채팅방이 존재하는 경우
			chatroomId = chatMember.getChatroomId();

			// 채팅내역 가져오기
			List<ChatLog> chatLogList = chatService.findChatLogByChatroomId(chatroomId);
			log.debug("chatLogList = {}", chatLogList);
			model.addAttribute("chatLogList", chatLogList);

		} else {
			// 채팅방에 처음 입장한 경우

			// chatroomId 생성
			chatroomId = getChatroomId();

			// 채팅방멤버 생성(member, admin)
			List<ChatMember> chatMemberList = Arrays.asList(
					new ChatMember(chatroomId, memberId),
					new ChatMember(chatroomId, "admin"));
			int result = chatService.createChatroom(chatMemberList);
		}
		log.debug("chatroomId = {}", chatroomId);
		model.addAttribute("chatroomId", chatroomId);
	}

	private String getChatroomId() {
		final int LEN = 8;

		Random rnd = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < LEN; i++) {
			if (rnd.nextBoolean()) {
				if (rnd.nextBoolean())
					sb.append((char)(rnd.nextInt(26) + 'A'));
				else
					sb.append((char)(rnd.nextInt(26) + 'a'));
			} else {
				sb.append(rnd.nextInt(10));
			}
		}
		return sb.toString();
	}

}
