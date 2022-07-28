package com.kh.spring.chat.model.service;

import java.util.List;
import java.util.Map;

import com.kh.spring.chat.model.dto.ChatLog;
import com.kh.spring.chat.model.dto.ChatMember;

public interface ChatService {

	int createChatroom(List<ChatMember> chatMemberList);

	ChatMember findChatMemberByMemberId(String memberId);

	int insertChatLog(Map<String, Object> payload);

	List<ChatLog> findChatLogByChatroomId(String chatroomId);

	List<ChatLog> findRecentChatLogList();

}
