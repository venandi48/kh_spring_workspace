package com.kh.spring.chat.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.chat.model.dao.ChatDao;
import com.kh.spring.chat.model.dto.ChatLog;
import com.kh.spring.chat.model.dto.ChatMember;

@Transactional(rollbackFor = Exception.class)
@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	ChatDao chatDao;

	@Override
	public int createChatroom(List<ChatMember> chatMemberList) {
		int result = 0;
		for (ChatMember chatMember : chatMemberList) {
			result = chatDao.insertChatMember(chatMember);
		}
		return result;
	}

	@Override
	public ChatMember findChatMemberByMemberId(String memberId) {
		return chatDao.findChatMemberByMemberId(memberId);
	}
	
	@Override
	public int insertChatLog(Map<String, Object> payload) {
		return chatDao.insertChatLog(payload);
	}

	@Override
	public List<ChatLog> findChatLogByChatroomId(String chatroomId) {
		return chatDao.findChatLogByChatroomId(chatroomId);
	}
	
	@Override
	public List<ChatLog> findRecentChatLogList() {
		return chatDao.findRecentChatLogList();
	}
	
	@Override
	public int updateLastCheck(Map<String, Object> payload) {
		return chatDao.updateLastCheck(payload);
	}
	
	@Override
	public int getUnreadCount(ChatMember chatMember) {
		return chatDao.getUnreadCount(chatMember);
	}
}
