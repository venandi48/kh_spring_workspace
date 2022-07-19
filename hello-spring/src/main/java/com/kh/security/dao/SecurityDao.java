package com.kh.security.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.spring.member.model.dto.Member;

@Mapper
public interface SecurityDao {

	Member loadUserByUsername(String username);

}
