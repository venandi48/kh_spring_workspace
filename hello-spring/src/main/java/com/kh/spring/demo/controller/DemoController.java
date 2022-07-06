package com.kh.spring.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.demo.model.service.DemoService;

@Controller
public class DemoController {

	@Autowired
	private DemoService demoService;
	
	/**
	 * 전송방식 GET(기본값)
	 */
	@RequestMapping("/demo/devForm.do")
	public String devForm() {
		System.out.println("GET /demo/devForm.do");
		System.out.println(12345);
		return "/demo/devForm";
	}
}
