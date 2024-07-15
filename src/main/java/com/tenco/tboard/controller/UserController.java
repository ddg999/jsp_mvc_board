package com.tenco.tboard.controller;

import java.io.IOException;

import com.tenco.tboard.repository.UserRepositoryImpl;
import com.tenco.tboard.repository.interfaces.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/user/*")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepository userRepository;

	// UserController --> (UserService) --> UserRepository

	@Override
	public void init() throws ServletException {
		userRepository = new UserRepositoryImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		switch (action) {
		case "/signup":
			request.getRequestDispatcher("/WEB-INF/views/user/signup.jsp").forward(request, response);
			break;
		case "/signin":
			// TODO 로그인 추가 예정
			break;
		case "/logout":
			// TODO 로그아웃 추가 예정
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
