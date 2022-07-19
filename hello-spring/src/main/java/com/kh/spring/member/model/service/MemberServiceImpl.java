package com.kh.spring.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.dto.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertMember(Member member) {
		int result = memberDao.insertMember(member);
		result = memberDao.insertAuthority(member); // ROLE_USER
		return result; 
	}
	
	@Override
	public Member selectOneMember(String memberId) {
		return memberDao.selectOneMember(memberId);
	}
	
	@Override
	public int updateMember(Member member) {
		return memberDao.updateMember(member);
	}

	
}
