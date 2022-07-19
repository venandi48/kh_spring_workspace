package com.kh.spring.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.spring.demo.model.dto.Dev;
import com.kh.spring.demo.model.exception.DevNotFoundException;
import com.kh.spring.demo.model.service.DemoService;

import lombok.extern.slf4j.Slf4j;

/**
 * RestAPI
 *  - Representational State Transfer API
 *  - 요청 성격별로 전송방식을 결정해서 사용하는 서비스
 *  - c POST
 *  - r GET
 *  - u PUT / PATCH
 *  - d DELETE
 *  
 *  - url작성 시에 명사형 계층구조를 갖도록 작성 
 *  	(ex. POST/dev = insertDev, GET/dev/102 = selectOneDev, PUT/dev/1 = updateDev, DELETE/dev/1 = deleteDev)
 */
@RequestMapping("/dev")
@Controller
@Slf4j
public class DevRestAPIController {

	@Autowired
	DemoService demoService;
	
	/**
	 * ResponseEntity<T>
	 *  - body에 작성할 자바타입
	 */
	// 방법1
	@GetMapping
	public ResponseEntity<?> dev() {
		List<Dev> list = null;
		try {
			list = demoService.selectDevList();
			log.debug("list = {}", list);
		} catch (Exception e) {
			log.error("Dev 목록 조회 오류", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "Dev 목록 조회 오류");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity.ok(list);
	}
	
	// 방법2
//	@GetMapping
	@ResponseBody
	public Object _dev() {
		List<Dev> list = null;
		try {
			list = demoService.selectDevList();
			log.debug("list = {}", list);
		} catch (Exception e) {
			log.error("Dev 목록 조회 오류", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "Dev 목록 조회 오류");
			return map;
		}
		return list;
	}
	
	
	@GetMapping("/{no}")
	public ResponseEntity<?> dev(@PathVariable int no) {
		Dev dev = null;
		try {
			log.debug("no = {}", no);
			dev = demoService.selectOneDev(no);
			log.debug("dev = {}", dev);
			
			if(dev == null) {
				throw new DevNotFoundException(String.valueOf(no));
			}
		} catch (DevNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Dev 한 명 조회 오류", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "Dev 조회 오류");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity.ok(dev);
	}
	
	/**
	 * @PathVariable 에 .이 포함된 경우 정규표현식으로 작성
	 */
	@GetMapping("/email/{email:.+}")
	public ResponseEntity<?> dev(@PathVariable String email) {
		Dev dev = null;
		try {
			log.debug("email = {}", email);
			// a. mvc요청 새로 만들기
			// dev = demoService.selectOneDevByEmail(email);
			List<Dev> list = demoService.selectDevList();
			
			// b. 전체목록에서 필터링
			for(Dev _dev : list) {
				if(email.equals(_dev.getEmail())) {
					dev = _dev;
					break;
				}
			}
			log.debug("dev = {}", dev);
			
			if(dev == null) {
				throw new DevNotFoundException(email);
			}
		} catch (DevNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("Dev 한 명 이메일 조회 오류", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "Dev 한명 이메일 조회 오류");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(dev);
	}
	
	
	@GetMapping("/lang/{lang}")
	public ResponseEntity<?> dev2(@PathVariable String lang) {
		List<Dev> list = new ArrayList<>();
		lang = lang.toLowerCase();
		try {
			List<Dev> allDev = demoService.selectDevList();
			for(Dev dev : allDev) {
				String[] langs = dev.getLang();
				List<String> langList = new ArrayList<>();
				for(String _lang : langs)
					langList.add(_lang.toLowerCase());
				
				if(langList.contains(lang)) {
					list.add(dev);
				}
			}
			log.debug("list = {}", list);
		} catch (DevNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("lang 사용가능 Dev 목록 조회 오류", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "lang 사용가능 Dev 목록 조회 오류");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(list);
	}
}
