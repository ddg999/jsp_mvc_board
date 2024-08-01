package com.tenco.tboard.controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.tenco.tboard.model.Comment;
import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.CommentRepositoryImpl;
import com.tenco.tboard.repository.interfaces.CommentRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/comment/*")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommentRepository commentRepository;

	@Override
	public void init() throws ServletException {
		commentRepository = new CommentRepositoryImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
			handleCreateComment(request, response, session);
			break;
		default:
			break;
		}
	}

	private void handleCreateComment(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
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
}
