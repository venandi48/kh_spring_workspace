package com.kh.spring.menu.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.spring.menu.model.dto.Menu;
import com.kh.spring.menu.model.dto.Taste;
import com.kh.spring.menu.model.dto.Type;

@Mapper
public interface MenuDao {

	@Select("select * from menu order by id desc")
	List<Menu> selectAll();

	@Select("select * from menu where type = #{type} order by id desc")
	List<Menu> selectMenuByType(Type type);

	@Select("select * from menu where taste = #{taste} order by id desc")
	List<Menu> selectMenuByTaste(Taste taste);

	@Insert("insert into menu values(seq_menu_id.nextval, #{restaurant}, #{name}, #{price}, #{type}, #{taste})")
	int insertMenu(Menu menu);

	@Select("select * from menu where id = #{id}")
	Menu selectOneMenu(int id);

	@Update("update menu "
			+ "set restaurant = #{restaurant}, name = #{name}, price = #{price}, type = #{type}, taste = #{taste} "
			+ "where id = #{id}")
	int updateMenu(Menu menu);

	@Delete("delete from menu where id = #{id}")
	int deleteMenu(int id);

}
