package com.kh.spring.data.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.data.corona19.Item;
import com.kh.spring.data.corona19.Response;
import com.kh.spring.data.course.Course;
import com.kh.spring.data.course.Student;
import com.kh.spring.data.model.service.DataService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/data")
public class DataController {

	@Autowired
	DataService dataService;

	final String XML_URL = "https://shqkel.github.io/course.xml";
	final String JSON_URL = "https://shqkel.github.io/course.json";
	final String CORONA19_URL = "http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson";
	final String SERVICE_KEY = "xG7FbVRiB%2FVRULA5s7YlsBsZOsoP1a%2BS%2BdHFBLAaDX%2FRj3QgOTytvSMvI7EbGXFYK0yIDMpKM97N9%2F9awD9dYg%3D%3D";

	@GetMapping("/xml/passXml.do")
	public ResponseEntity<?> passXml() {
		Resource resource = dataService.passXML(XML_URL);
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
				.body(resource);
	}
	
	@GetMapping("/xml/parseXml.do")
	public ResponseEntity<?> parseXml() {
		// 1. 자원요청
//		Course course = dataService.parseXml(XML_URL);
//		Course course = dataService.parseXml2(XML_URL);
		Course course = dataService.parseXml2(XML_URL, Course.class);
		log.debug("course = {}", course);
		
		// 2. 데이터 전처리.
		List<Student> students = course.getStudents();
		// 인터페이스 - 클래스선언 & 객체선언
		// 인터페이스 구현체로 동적으로 생성하여 처리
		students.sort(new Comparator<Student>() {
			@Override
			public int compare(Student o1, Student o2) {
				return o2.getId() - o1.getId();
			}
		});
		
		return ResponseEntity.ok(students);
	}

	@GetMapping("/corona19.do")
	public ResponseEntity<?> corona19() {
		String url = CORONA19_URL + "?serviceKey=" + SERVICE_KEY;
//		Response response = dataService.getCorona19(url);
		Response response = dataService.getCorona19XmlMapper(url);
		log.debug("response = {}", response);
		List<Item> items = response.getBody().getItems();
		items.sort(new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				return o2.getIncDec() - o1.getIncDec();
			}
		});
		
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/json/passJson.do")
	public ResponseEntity<?> passJson() {
		return ResponseEntity.ok(dataService.passJson(JSON_URL));
	}

	@GetMapping("/json/parseJson.do")
	public ResponseEntity<?> parseJson() {
		Course course = dataService.parseJson(JSON_URL);
		log.debug("course = {}", course);
		return ResponseEntity.ok(course);
	}
	
	
}
