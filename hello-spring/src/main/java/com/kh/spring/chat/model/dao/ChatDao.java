package com.kh.spring.chat.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.spring.chat.model.dto.ChatLog;
import com.kh.spring.chat.model.dto.ChatMember;

@Mapper
public interface ChatDao {

	@Insert("insert into chat_member (chatroom_id, member_id) values (#{chatroomId}, #{memberId})")
	int insertChatMember(ChatMember chatMember);

	@Select("select * from chat_member where member_id = #{member_id}")
	ChatMember findChatMemberByMemberId(String memberId);

	@Insert("insert into chat_log values (seq_chat_log_no.nextval, #{chatroomId}, #{memberId}, #{msg}, #{time})")
	int insertChatLog(Map<String, Object> payload);

	@Select("select * from chat_log where chatroom_id = #{chatroomId} order by time")
	List<ChatLog> findChatLogByChatroomId(String chatroomId);

	List<ChatLog> findRecentChatLogList();

}
