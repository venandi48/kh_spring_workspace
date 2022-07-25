package com.kh.spring.menu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	ResourceLoader resourceLoader;
	
	final String MENU_URL = "http://localhost:10000/springboot/menu";

	@GetMapping("/menu.do")
	public void menu() {}
	
	@GetMapping("/selectMenuByType.do")
	public ResponseEntity<?> selectMenuByType(@RequestParam String type) {
		log.debug("type = {}", type);
		
		// springboot(rest api)로 요청
		String url = MENU_URL + "/type/" + type;
		Resource resource = resourceLoader.getResource(url);
		return ResponseEntity.ok(resource);
	}
	
	@GetMapping("/selectMenuByTaste.do")
	public ResponseEntity<?> selectMenuByTaste(@RequestParam String taste) {
		log.debug("taste = {}", taste);
		
		// springboot(rest api)로 요청
		String url = MENU_URL + "/taste/" + taste;
		Resource resource = resourceLoader.getResource(url);
		return ResponseEntity.ok(resource);
	}
	
	/**
	 * RestAPI에 대해 GET, POST, PUT, DELETE 요청 가능
	 * 
	 * RestTemplate (3.x ~)
	 * WebClient (5.x ~)
	 */
	@PostMapping("/insertMenu.do")
	public ResponseEntity<?> insertMenu(@RequestBody Map<String, Object> menu) {
		log.debug("menu = {}", menu);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Map<String, Object>> request = new HttpEntity(menu); // map -> json
		Map<String, Object> response = restTemplate.postForObject(MENU_URL, request, Map.class);
		return ResponseEntity.ok(response);
	}
	
}
