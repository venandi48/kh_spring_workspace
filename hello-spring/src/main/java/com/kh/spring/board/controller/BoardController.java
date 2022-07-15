package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.board.model.dto.Attachment;
import com.kh.spring.board.model.dto.Board;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.common.HelloSpringUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	ServletContext application;
	
	@GetMapping("/boardList.do")
	public ModelAndView boardList(
			@RequestParam(defaultValue = "1") int cPage,
			ModelAndView mav,
			HttpServletRequest request) {
		try {
			// 목록조회
			int numPerPage = 5;
			List<Board> list = boardService.selectBoardList(cPage, numPerPage);
			// log.debug("list = {}", list);
			mav.addObject("list", list);
			
			// 페이지바
			int totalContent = boardService.selectTotalContent();
			// log.debug("totalContent = {}", totalContent);
			String url = request.getRequestURI();
			String pagebar = HelloSpringUtils.getPagebar(cPage, numPerPage, totalContent, url);
			// log.debug("pagebar = {}", pagebar);
			mav.addObject("pagebar", pagebar);
			
			// viewName 설정
			mav.setViewName("board/boardList");
		} catch(Exception e) {
			log.error("게시글 목록 조회 오류", e);
			throw e;
		}
		return mav;
	}

	@GetMapping("/boardForm.do")
	public void boardForm() { }
	
	@PostMapping("/boardEnroll.do")
	public String boardEnroll(
			Board board,
			@RequestParam("upFile") MultipartFile[] upFiles,
			RedirectAttributes redirectAttr) {
		try {
			//log.debug("board = {}", board);
			//log.debug("application = {}", application);
			//log.debug("saveDirectory = {}", saveDirectory);
			
			String saveDirectory = application.getRealPath("/resources/upload/board");
			
			// 업로드한 파일 저장
			for(MultipartFile upFile : upFiles) {
				if(upFile.getSize() > 0) {
					String originalFilename = upFile.getOriginalFilename();
					String renamedFilename = HelloSpringUtils.getRenamedFilename(originalFilename);
					log.debug("renamedFilename = {}", renamedFilename);
					
					File destFile = new File(saveDirectory, renamedFilename);
					upFile.transferTo(destFile);
					
					// Attachment객체 -> Board#attachments에 추가
					Attachment attach = new Attachment();
					attach.setOriginalFilename(originalFilename);
					attach.setRenamedFilename(renamedFilename);
					board.addAttachment(attach);
				}
			}
			
			int result = boardService.insertBoard(board);
			
			redirectAttr.addFlashAttribute("msg", "게시글을 성공적으로 등록했습니다.");
		} catch(IOException e) {
			log.error("첨부파일 저장 오류", e);
		} catch (Exception e) {
			log.error("게시글 등록 오류", e);
			throw e;
		}

		return "redirect:/board/boardDetail.do?no=" + board.getNo();
	}
	
	@GetMapping("/boardDetail.do")
	public ModelAndView boardDetail(@RequestParam int no, ModelAndView mav) {
		try {
//			Board board = boardService.selectOneBoard(no);
			Board board = boardService.selectOneBoardCollection(no);
			log.debug("board = {}", board);
			mav.addObject("board", board);
			
			mav.setViewName("board/boardDetail");
		} catch (Exception e) {
			log.error("게시글 조회 오류", e);
			throw e;
		}
		return mav;
	}
	
	@GetMapping("/boardUpdate.do")
	public void boardUpdate(@RequestParam int no,Model model) {
		try {
			Board board = boardService.selectOneBoard(no);
			log.debug("board = {}", board);
			model.addAttribute("board", board);
		} catch (Exception e) {
			log.error("게시글 수정폼 오류", e);
			throw e;
		}
	}
	
	@PostMapping("/boardUpdate.do")
    public String boardUpdate(
            @ModelAttribute Board board,
            @RequestParam("upFile") MultipartFile[] upFiles,
            @RequestParam(value="delFile", required=false) int[] delFiles,
            RedirectAttributes redirectAttr) {
		
        try {
			int result = 0;
			log.debug("board = {}", board);

			// 1. 첨부파일 삭제 (파일 삭제 + table 행삭제)
			// 복수개의 delFile처리
			String saveDirectory = application.getRealPath("/resources/upload/board");
			if(delFiles != null) {
				for(int attachNo : delFiles) {
					Attachment attach = boardService.selectOneAttachment(attachNo);
					
					result = boardService.deleteAttachments(attachNo);
					
					File delFile = new File(saveDirectory, attach.getRenamedFilename());
					if(delFile.exists())
						delFile.delete();
				}
			}
			
			// 2. 첨부파일 등록 (파일 저장 + Attachment객체생성 추가)
			for(MultipartFile upFile : upFiles) {
				if(upFile.getSize() > 0) {
					String originalFilename = upFile.getOriginalFilename();
					String renamedFilename = HelloSpringUtils.getRenamedFilename(originalFilename);
					// log.debug("renamedFilename = {}", renamedFilename);
					
					File destFile = new File(saveDirectory, renamedFilename);
					upFile.transferTo(destFile);
					
					// Attachment객체 -> Board#attachments에 추가
					Attachment attach = new Attachment();
					attach.setOriginalFilename(originalFilename);
					attach.setRenamedFilename(renamedFilename);
					board.addAttachment(attach);
				}
			}
			
			// 3. 게시글 수정 (board수정 + 복수개의 attachment 등록)
			result = boardService.updateBoard(board);

		} catch (Exception e) {
			log.error("게시글 수정 오류", e);
		}
        
        return "redirect:/board/boardDetail.do?no=" + board.getNo(); // board#no
    }
	
}
