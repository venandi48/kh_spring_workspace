package com.kh.spring.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.dto.Member;
import com.kh.spring.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
@SessionAttributes({"loginMember", "next"})
public class MemberController {
	
//	private static final Logger log = LoggerFactory.getLogger(MemberController.class);

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
	
	/**
	 * $2a$10$.eu3p.QmAQtq08IJPZuAJOacnGuqPvk6mTrC1gR8cXY8hNDUBru6q
	 *  - 알고리즘 $2a$
	 *  - 옵션 10$ 속도/메모리 요구량
	 *  - 랜덤솔트 22자리 .eu3p.QmAQtq08IJPZuAJO
	 *  - 해시 31자리 acnGuqPvk6mTrC1gR8cXY8hNDUBru6q
	 */
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
	
	/**
	 * ViewNameTranslator빈
	 *  - 핸들러의 리턴타입이 void인 경우, 요청주소를 바탕으로 viewName을 유추
	 *  - /memeber/memberLogin.do -> member/memberLogin -> /WEB-INF/views/member/memberLogin.jsp
	 */
	@GetMapping("/memberLogin.do")
	public void memberLogin(
			@RequestHeader(name="Referer", required = false) String referer,
			Model model) {
		log.info("referer = {}", referer);
		
		if(referer != null)
			model.addAttribute("next", referer);

	}
	
	@PostMapping("/memberLogin.do")
	public String memberLogin(
			@RequestParam String memberId,
			@RequestParam String password,
			RedirectAttributes redirectAttr,
			@SessionAttribute(required = false) String next,
			Model model) {

		log.info("memberId = {}, password= {}", memberId, password);
		
		try {
			Member member = memberService.selectOneMember(memberId);

			if (member != null && bcryptPasswordEncoder.matches(password, member.getPassword())) {
//				redirectAttr.addFlashAttribute("msg", "로그인 성공!");
				model.addAttribute("loginMember", member);
				
				log.info("next = {}", next);
				// model.addAttribute("next", null); // model에서 제거 불가
				String location = next != null ? next : "/";

				return "redirect:" + location;
			} else {
				redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
				return "redirect:/member/memberLogin.do";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@GetMapping("/memberLogout.do")
	public String memberLogout(SessionStatus sessionStatus, ModelMap modelMap) {
		// modelMap 속성 완전 제거
		modelMap.clear();
		
		// 사용완료 마킹처리 (세션객체 자체를 폐기하지 않는다)
		if(!sessionStatus.isComplete())
			sessionStatus.setComplete();
		
		return "redirect:/";
	}
	
	@GetMapping("memberDetail.do")
	public void memberDetail() { }
	
	@PostMapping("memberUpdate.do")
	public String memberUpdate(
			@ModelAttribute("loginMember") Member loginMember, 
			RedirectAttributes redirectAttr, 
			Model model) {
		// log.info("member = {}", member);

		try {
			log.info("loginMember = {}", loginMember);
			int result = memberService.updateMember(loginMember);

			redirectAttr.addFlashAttribute("msg", "회원정보가 수정되었습니다.");
			
			// 세션 정보 갱신 방법 1 : 파라미터에 Member member, Model model 포함하여 사용
//			Member updatedMember = memberService.selectOneMember(member.getMemberId());
//			model.addAttribute("loginMember", updatedMember);
			
//			log.info("updatedMember = {}", updatedMember);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return "redirect:/member/memberDetail.do";
	}
}
