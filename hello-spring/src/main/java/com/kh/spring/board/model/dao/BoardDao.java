package com.kh.spring.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import com.kh.spring.board.model.dto.Board;

@Mapper
public interface BoardDao {

	List<Board> selectBoardList(RowBounds rowBounds);

	@Select("select count(*) from board")
	int selectTotalContent();

	@Select("insert into board (no, title, member_id, content) values (seq_board_no.nextval, #{title}, #{memberId}, #{content})")
	int insertBoard(Board board);

}
