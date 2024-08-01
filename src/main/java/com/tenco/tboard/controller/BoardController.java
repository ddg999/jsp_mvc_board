package com.tenco.tboard.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.tenco.tboard.model.Board;
import com.tenco.tboard.model.Comment;
import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.BoardRepositoryImpl;
import com.tenco.tboard.repository.CommentRepositoryImpl;
import com.tenco.tboard.repository.interfaces.BoardRepository;
import com.tenco.tboard.repository.interfaces.CommentRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardRepository boardRepository;
	private CommentRepository commentRepository;

	@Override
	public void init() throws ServletException {
		boardRepository = new BoardRepositoryImpl();
		commentRepository = new CommentRepositoryImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("principal") == null) {
			response.sendRedirect(request.getContextPath() + "/user/signin");
			return;
		}

		String action = request.getPathInfo();
		switch (action) {
		case "/create":
			showCreateBoardForm(request, response, session);
			break;
		case "/list":
			handleListBoards(request, response, session);
			break;
		case "/view":
			showViewBoard(request, response, session);
			break;
		case "/update":
			showUpdateBoardForm(request, response, session);
			break;
		case "/delete":
			handleDeleteBoard(request, response, session);
			break;
		case "/deleteComment":
			handleDeleteComment(request, response, session);
			break;
		case "/updateComment":
			showUpdateComment(request, response, session);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	private void showUpdateComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		String content = request.getParameter("content");
	}

	// 게시글 상세보기 화면 이동
	private void showViewBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			int boardId = Integer.parseInt(request.getParameter("id"));
			Board board = boardRepository.getBoardById(boardId);
			if (board == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// 현재 로그인한 사용자의 ID
			User user = (User) session.getAttribute("principal");
			if (user != null) {
				request.setAttribute("userId", user.getId());
			}

			// TODO - 댓글 조회
			// 댓글 조회 및 권한 확인 추가 예정
			List<Comment> commentList = commentRepository.getCommentByBoardId(boardId, 100, 0);

			request.setAttribute("board", board);
			request.setAttribute("commentList", commentList);
			request.getRequestDispatcher("/WEB-INF/views/board/view.jsp").forward(request, response);
		} catch (Exception e) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('잘못된 접근입니다'); history.back();</script>");
			e.printStackTrace();
		}
	}

	// 게시글 삭제 기능 처리 (GET 방식)
	private void handleDeleteBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
//		response.setContentType("text/html;charset=UTF-8");
//		PrintWriter out = response.getWriter();
//		out.println("<script>alert('정말 삭제하시겠습니까?');</script>");
		boardRepository.deleteBoard(Integer.parseInt(request.getParameter("boardId")));
		response.sendRedirect(request.getContextPath() + "/board/list");
	}

	// 댓글 삭제 기능 처리 (GET 방식)
	private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {

		commentRepository.deleteComment(Integer.parseInt(request.getParameter("commentId")));
		response.sendRedirect(request.getContextPath() + "/board/view?id=" + request.getParameter("boardId"));
	}

	// 게시글 수정 화면 이동 (인증 검사 필수)
	private void showUpdateBoardForm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		try {
			int boardId = Integer.parseInt(request.getParameter("boardId"));
			Board board = boardRepository.getBoardById(boardId);
			if (board == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// 현재 로그인한 사용자의 ID
			User user = (User) session.getAttribute("principal");
			if (user != null) {
				request.setAttribute("userId", user.getId());
			}

			request.setAttribute("board", board);
			request.getRequestDispatcher("/WEB-INF/views/board/update.jsp").forward(request, response);
		} catch (Exception e) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('잘못된 접근입니다'); history.back();</script>");
			e.printStackTrace();
		}
	}

	// 게시글 생성 화면 이동
	private void showCreateBoardForm(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/board/create.jsp").forward(request, response);
	}

	// 페이징 처리하기
	private void handleListBoards(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// 게시글 목록 조회 기능
		int page = 1; // 기본 페이지 번호
		int pageSize = 3; // 한 페이지당 보여질 게시글의 수
		try {
			String pageStr = request.getParameter("page");
			if (pageStr != null) {
				page = Integer.parseInt(pageStr);
			}
		} catch (Exception e) {
			// 유효하지 않은 번호를 마음대로 보낼 경우
			page = 1;
			e.printStackTrace();
		}
		int offset = (page - 1) * pageSize; // 시작 위치 계산

		List<Board> boardList = boardRepository.getAllBoards(pageSize, offset);
		request.setAttribute("boardList", boardList);

		// 페이징 처리 1단계 (현재 페이지 번호, pageSize, offset)

		// 전체 게시글 수 조회
		int totalBoards = boardRepository.getTotalBoardCount();

		// 총 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalBoards / pageSize);
		request.setAttribute("boardList", boardList);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("currentPage", page);

		// 현재 로그인한 사용자 ID 설정
		if (session != null) {
			User user = (User) session.getAttribute("principal");
			if (user != null) {
				request.setAttribute("userId", user.getId());
			}
		}

		request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("principal") == null) {
			response.sendRedirect(request.getContextPath() + "/user/signin");
			return;
		}

		String action = request.getPathInfo();
		switch (action) {
		case "/create":
			handleCreateBoard(request, response, session);
			break;
		case "/edit":
			handleEditBoard(request, response, session);
			break;
		case "/addComment":
			HandleAddComment(request, response, session);
			break;
		case "/update":
			handleUpdateBoard(request, response, session);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	// 게시글 수정 기능
	private void handleUpdateBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		int boardId = Integer.parseInt(request.getParameter("boardId"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		Board board = Board.builder().id(boardId).title(title).content(content).build();
		boardRepository.updateBoard(board);

		response.sendRedirect(request.getContextPath() + "/board/list?page=1");

	}

	// 댓글 추가 기능
	private void HandleAddComment(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		int boardId = Integer.parseInt(request.getParameter("boardId"));
		String content = request.getParameter("content");
		User user = (User) session.getAttribute("principal");

		Comment comment = Comment.builder().boardId(boardId).userId(user.getId()).content(content).build();
		commentRepository.addComment(comment);

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script>alert('댓글이 등록되었습니다');</script>");

		response.sendRedirect(request.getContextPath() + "/board/view?id=" + boardId);
	}

	private void handleEditBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	}

	// 게시글 추가 기능
	private void handleCreateBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		// 유효성 검사 생략
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		User user = (User) session.getAttribute("principal");

		Board board = Board.builder().user(user.getId()).title(title).content(content).build();
		boardRepository.addBoard(board);
		response.sendRedirect(request.getContextPath() + "/board/list?page=1");
	}
}
