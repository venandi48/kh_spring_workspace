package com.kh.spring.menu.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.menu.model.dao.MenuDao;
import com.kh.spring.menu.model.dto.Menu;
import com.kh.spring.menu.model.dto.Taste;
import com.kh.spring.menu.model.dto.Type;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	MenuDao menuDao;
	
	@Override
	public List<Menu> selectAll() {
		return menuDao.selectAll();
	}
	
	@Override
	public List<Menu> selectMenuByType(Type type) {
		return menuDao.selectMenuByType(type);
	}
	
	@Override
	public List<Menu> selectMenuByTaste(Taste taste) {
		return menuDao.selectMenuByTaste(taste);
	}
	
	@Override
	public int insertMenu(Menu menu) {
		return menuDao.insertMenu(menu);
	}
}
