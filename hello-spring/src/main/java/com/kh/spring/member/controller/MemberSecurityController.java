package com.kh.spring.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

	/**
	 * SecurityContextHolder - SecurityContext - Authentication에 보관중인 로그인한 사용자 정보 가져오기
	 * 
	 * - Principal
	 * - Credentials
	 * - Authorities
	 * 
	 * 1. SecurityContextHolder로 부터 Authentication 가져오기
	 * 2. 핸들러의 매개인자로 Authentication 받기
	 * 3. @AuthenticationPrincipal 통해서 Principal객체 받기
	 */
	@GetMapping("memberDetail.do")
//	public void memberDetail(Authentication authentication) {
////		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 매개변수로 받아오지 않은 경우에 사용
//		Member principal = (Member) authentication.getPrincipal(); // principal = Member(super=MemberEntity(memberId=honggd, password=$2a$10$.eu3p.QmAQtq08IJPZuAJOacnGuqPvk6mTrC1gR8cXY8hNDUBru6q, name=홍길동, gender=M, birthday=1999-09-05, email=honggd@gmail.com, phone=01012341234, address=null, hobby=[운동, 독서, 게임, 여행], createdAt=2022-07-11T14:13:08, updatedAt=2022-07-12T12:50:37, enabled=true), authorities=[ROLE_USER])
//		log.debug("principal = {}", principal);
//		
//		Object credentials = authentication.getCredentials();
//		log.debug("credentials = {}", credentials); // credentials = null
//		
//		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
//		log.debug("authorities = {}", authorities); // authorities = [ROLE_USER]
//	}
	public void memberDetail(@AuthenticationPrincipal Member member) {
		log.debug("member = {}", member);
	}
	
	@PostMapping("/memberUpdate.do")
	public ResponseEntity<?> memberUpdate(Member updateMember, @AuthenticationPrincipal Member loginMember) {
		log.debug("updateMember = {}", updateMember);
		log.debug("loginMember = {}", loginMember);
		Map<String, Object> map = new HashMap<>();
		
		try {
			// 1. DB갱신
			int result = memberService.updateMember(updateMember);
			
			// 2. security가 관리하는 session 업데이트
			loginMember.setName(updateMember.getName());
			loginMember.setBirthday(updateMember.getBirthday());
			loginMember.setEmail(updateMember.getEmail());
			loginMember.setPhone(updateMember.getPhone());
			loginMember.setAddress(updateMember.getAddress());
			loginMember.setGender(updateMember.getGender());
			loginMember.setHobby(updateMember.getHobby());
			
			// 비밀번호 | 권한정보가 바뀌었을때는 전체 Authentication을 대체
			Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
													loginMember, loginMember.getPassword(), loginMember.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			
			map.put("msg", "회원정보를 성공적으로 수정했습니다.");
		} catch (Exception e) {
			log.error("회원정보 수정 오류", e);
			map.put("msg", "회원정보 수정 오류!");
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
					.body(map);
		}
		
		return ResponseEntity.ok(map);
	}
	
}
