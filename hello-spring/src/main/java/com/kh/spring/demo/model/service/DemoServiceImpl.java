package com.kh.spring.demo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.demo.model.dao.DemoDao;
import com.kh.spring.demo.model.dto.Dev;

/**
 * Service단의 역할
 *  - SqlSession생성/반환 -> Dao DI(의존주입)받아서 처리
 *  - dao요청
 *  - 트랜잭션 처리 -> AOP로 구현
 */
@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoDao demoDao;
	
	@Override
	public int insertDev(Dev dev) {
		return demoDao.insertDev(dev);
	}
	
	@Override
	public List<Dev> selectDevList() {
		return demoDao.selectDevList();
	}
	
	@Override
	public int deleteDev(int no) {
		return demoDao.deleteDev(no);
	}
	
	@Override
	public Dev selectOneDev(int no) {
		return demoDao.selectOneDev(no);
	}
	
	@Override
	public int updateDev(Dev dev) {
		return demoDao.updateDev(dev);
	}
}
