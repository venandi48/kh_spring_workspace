package com.kh.spring.menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.spring.menu.model.dto.Menu;
import com.kh.spring.menu.model.dto.Taste;
import com.kh.spring.menu.model.dto.Type;
import com.kh.spring.menu.model.service.MenuService;

import lombok.extern.slf4j.Slf4j;

/**
 * GET /menu
 * GET /menu/kr
 * GET /menu/ch
 * GET /menu/jp
 * 
 * GET /menu/10
 * 
 * POST /menu
 * 
 * PUT /menu
 * 
 * DELETE /menu/10
 *
 * @RestController : @Controller + 모든 핸들러에 @ResponseBody 추가
 * 
 */
@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	MenuService menuService;
	
	@CrossOrigin(origins = "*")
	@GetMapping
	public List<Menu> selectAll(HttpServletResponse response) {
		// CORS정책 -Access-Control-Allow-Origin 헤더값 추가
//		response.addHeader("Access-Control-Allow-Origin", "http://localhost:9090");
//		response.addHeader("Access-Control-Allow-Origin", "*");
		return menuService.selectAll();
	}
	
	// Enum 타입 매개변수 사용
	@GetMapping("/type/{type}")
	public List<Menu> selectMenuByType(@PathVariable Type type) {
		log.debug("type = {}", type);
		return menuService.selectMenuByType(type);
	}
	
	@GetMapping("/taste/{taste}")
	public List<Menu> selectMenuByTaste(@PathVariable Taste taste) {
		return menuService.selectMenuByTaste(taste);
	}
	
	@PostMapping
	public ResponseEntity<?> insertMenu(@RequestBody Menu menu) {
		log.debug("menu = {}", menu);
		Map<String, Object> map = new HashMap<>();
		try {
			int result = menuService.insertMenu(menu);
			map.put("msg", "메뉴를 정상적으로 등록했습니다.");
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			log.error("메뉴 등록 오류!", e);
			map.put("msg", "메뉴 등록 오류!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> selectOneMenu(@PathVariable int id) {
		Menu menu = null;
		try {
			menu = menuService.selectOneMenu(id);
			
			if(menu == null)
				return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error("메뉴 조회 오류!", e);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "메뉴 조회 오류");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
		return ResponseEntity.ok(menu);
	}
	
	@PutMapping
	public ResponseEntity<?> updateMenu(@RequestBody Menu menu) {
		try {
			log.debug("menu = {}", menu);
			int result = menuService.updateMenu(menu);
			Map<String, Object> map = new HashMap<>();
			map.put("msg", "메뉴를 성공적으로 수정했습니다.");
			
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			log.error("메뉴 수정 오류!", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMenu(@PathVariable int id) {
		try {
			log.debug("id = {}", id);
			int result = menuService.deleteMenu(id);
			if(result > 0) {
				Map<String, Object> map = new HashMap<>();
				map.put("msg", "메뉴를 성공적으로 삭제했습니다.");
				return ResponseEntity.ok(map);
			} else {
				return ResponseEntity.notFound().build();
			}
			
		} catch (Exception e) {
			log.error("메뉴 삭제 오류!", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}
