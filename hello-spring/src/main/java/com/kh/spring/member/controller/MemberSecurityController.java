package com.kh.spring.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberSecurityController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;
	
//	@RequestMapping(path = "/memberEnroll.do", method = RequestMethod.GET)
	@GetMapping("/memberEnroll.do")
	public String memberEnroll() {
		log.info(null);
		return "member/memberEnroll";
	}
	

	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		log.info("member = {}", member);
		try {
			// 암호화처리
			String rowPassword = member.getPassword();
			String encryptedPassword = bcryptPasswordEncoder.encode(rowPassword);
			member.setPassword(encryptedPassword);
			log.info("encryptedPassword = {}", encryptedPassword);
			
			int result = memberService.insertMember(member);
			redirectAttr.addFlashAttribute("msg", "회원가입이 정상적으로 완료되었습니다.");
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "redirect:/";
	}
	
	@GetMapping("/checkIdDuplicate.do")
	public ResponseEntity<?> checkIdDuplicate3(@RequestParam String memberId) {
		Map<String, Object> map = new HashMap<>();
		try {
			Member member = memberService.selectOneMember(memberId);
			boolean available = member == null;
			
			map.put("memberId", memberId);
			map.put("available", available);
		} catch (Exception e) {
			log.error("아이디 중복체크 오류", e);
			// throw e;
			
			map.put("error", e.getMessage());
			// map.put("msg", "이용에 불편을 드려 죄송합니다.");
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
					.body(map);
		}
		// return ResponseEntity.ok(map); // 200 + body에 작성할 map
		return ResponseEntity
				.status(HttpStatus.OK) // 200
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(map);
	}
	
	@GetMapping("/memberLogin.do")
	public void memberLogin() {	}

}
