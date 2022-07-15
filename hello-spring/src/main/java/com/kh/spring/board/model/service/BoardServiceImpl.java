package com.kh.spring.board.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.dto.Attachment;
import com.kh.spring.board.model.dto.Board;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Board> selectBoardList(int cPage, int numPerPage) {
		int offset = (cPage - 1) * numPerPage; // 얼마를 건너뛸 것인가
		int limit = numPerPage;
		RowBounds rowBounds = new RowBounds(offset, limit);
		return boardDao.selectBoardList(rowBounds);
	}
	
	@Override
	public int selectTotalContent() {
		return boardDao.selectTotalContent();
	}
	
	/**
	 * @Transactional은 기본적으로 Runtime 예외가 발생한 경우만 rollback처리
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertBoard(Board board) {

		// 1. board insert
		int result = boardDao.insertBoard(board);
		log.debug("board#no = {}", board.getNo());

		// 2. attachments insert
		List<Attachment> attachments = board.getAttachments();
		if (!attachments.isEmpty()) {
			for (Attachment attach : attachments) {
				attach.setBoardNo(board.getNo());
				result = boardDao.insertAttachment(attach);
			}
		}

		return result;
	}
	
	@Override
	public Board selectOneBoard(int no) {
		Board board = boardDao.selectOneBoard(no);
		List<Attachment> attachs = boardDao.selectAttachmentListByBoardNo(no);
		board.setAttachCount(attachs.size());
		board.setAttachments(attachs);
		
		return board;
	}
	
	@Override
	public Board selectOneBoardCollection(int no) {
		return boardDao.selectOneBoardCollection(no);
	}
	
	@Override
	public Attachment selectOneAttachment(int attachNo) {
		return boardDao.selectOneAttachment(attachNo);
	}
	
	@Override
	public int deleteAttachments(int attachNo) {
		return boardDao.deleteAttachments(attachNo);
	}
	
	@Override
	public int updateBoard(Board board) {
		
		// 1. board update
		int result = boardDao.updateBoard(board);

		// 2. attachments insert
		List<Attachment> attachments = board.getAttachments();
		if (!attachments.isEmpty()) {
			for (Attachment attach : attachments) {
				attach.setBoardNo(board.getNo());
				result = boardDao.insertAttachment(attach);
			}
		}

		return result;
	}

}
