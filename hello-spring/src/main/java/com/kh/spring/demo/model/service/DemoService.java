package com.kh.spring.demo.model.service;

import java.util.List;

import com.kh.spring.demo.model.dto.Dev;

public interface DemoService {

	int insertDev(Dev dev);

	List<Dev> selectDevList();

}
