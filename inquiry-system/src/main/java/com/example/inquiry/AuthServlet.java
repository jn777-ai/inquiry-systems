package com.example.inquiry;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

	private static final String ADMIN_USER = "admin";//ここでログインに使うユーザー名を指定
	private static final String ADMIN_PASS = "123";//パスワードを指定

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");//それぞれ受け取ったものを二つの変数に代入している
		if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) { //上で指定したユーザー名・パスワードと代入したものが一致していたら以下の処理
			HttpSession session = req.getSession();
			session.setAttribute("isAdmin", true);//InquiryServletでhistory.jspに遷移するときに利用する
			resp.sendRedirect(req.getContextPath() + "/inquiry?action=history");//リダイレクトの命令内でパスを指定している
		} else { //一致していなければ以下の処理
			req.setAttribute("loginError", "ユーザー名またはパスワードが間違っています。");
			req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
		}
	}
}
