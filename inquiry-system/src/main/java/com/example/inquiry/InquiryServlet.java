package com.example.inquiry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/inquiry")
public class InquiryServlet extends HttpServlet {
	private final InquiryDAO inquiryDAO = new InquiryDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("action");

		if ("history".equals(action)) {
			HttpSession session = req.getSession(false);
			Boolean isAdmin = false;
			if (session != null && session.getAttribute("isAdmin") instanceof Boolean) {
				isAdmin = (Boolean) session.getAttribute("isAdmin");
			}

			if (!isAdmin) {
				resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp");
				return;
			}

			String genreFilter = req.getParameter("genre");
			String statusFilter = req.getParameter("status");

			List<Inquiry> filteredInquiries;
			try {
				if ((genreFilter != null && !genreFilter.isEmpty()) ||
						(statusFilter != null && !statusFilter.isEmpty())) {
					filteredInquiries = inquiryDAO.getInquiriesByGenreAndStatus(genreFilter, statusFilter);
					req.setAttribute("selectedGenre", genreFilter);
					req.setAttribute("selectedStatus", statusFilter);
				} else {
					filteredInquiries = inquiryDAO.getAllInquiries();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				req.setAttribute("errorMessage", "DBエラーが発生しました。");
				RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
				rd.forward(req, resp);
				return;
			}

			Collections.reverse(filteredInquiries);

			req.setAttribute("inquiries", filteredInquiries);
			RequestDispatcher rd = req.getRequestDispatcher("/jsp/inquiry_history.jsp");
			rd.forward(req, resp);

		} else {

			generateCaptcha(req);
			RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");
			rd.forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		HttpSession session = req.getSession();

		if ("complete".equals(action)) {
			Inquiry inquiry = (Inquiry) session.getAttribute("inquiry");
			if (inquiry != null) {
				try {
					inquiryDAO.addInquiry(inquiry);
				} catch (SQLException e) {
					e.printStackTrace();
					req.setAttribute("errorMessage", "DBエラーが発生しました。");
					RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
					rd.forward(req, resp);
					return;
				}
			}
			session.removeAttribute("inquiry");
			RequestDispatcher rd = req.getRequestDispatcher("/jsp/complete.jsp");
			rd.forward(req, resp);

		} else if ("updateStatus".equals(action)) {

			String idStr = req.getParameter("id");
			String newStatus = req.getParameter("newStatus");

			try {
				int id = Integer.parseInt(idStr);
				inquiryDAO.updateInquiryStatus(id, newStatus);
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
				req.setAttribute("errorMessage", "ステータス更新に失敗しました。");
				RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
				rd.forward(req, resp);
				return;
			}

			resp.sendRedirect(req.getContextPath() + "/inquiry?action=history");

		} else if ("delete".equals(action)) {

			String idStr = req.getParameter("id");
			try {
				int id = Integer.parseInt(idStr);
				inquiryDAO.deleteInquiryById(id);
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
				req.setAttribute("errorMessage", "削除に失敗しました。");
				RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");
				rd.forward(req, resp);
				return;
			}

			resp.sendRedirect(req.getContextPath() + "/inquiry?action=history");

		} else {
			String name = req.getParameter("name");
			String employeeId = req.getParameter("employeeId");
			String genre = req.getParameter("genre");
			String content = req.getParameter("content");
			String captchaInput = req.getParameter("captcha");

			Map<String, String> errors = new HashMap<>();

			if (name != null && name.trim().isEmpty()) {
				name = null;
			}

			if (employeeId == null || !employeeId.matches("^\\d{6}$")) {
				errors.put("employeeId", "社員番号は6桁の数字で入力してください。");
			}

			if (genre == null || genre.trim().isEmpty()) {
				errors.put("genre", "ジャンルを選択してください。");
			}

			if (content == null || content.trim().isEmpty()) {
				errors.put("content", "内容は必須です。");
			}

			if (!verifyCaptcha(req, captchaInput)) {
				errors.put("captcha", "CAPTCHA が不正です。");
			}

			Inquiry inquiry = new Inquiry();
			inquiry.setName(name);
			inquiry.setEmployeeId(employeeId);
			inquiry.setGenre(genre);
			inquiry.setContent(content);
			inquiry.setStatus("新規");

			if (!errors.isEmpty()) {
				req.setAttribute("errors", errors);
				req.setAttribute("inquiry", inquiry);
				generateCaptcha(req);
				RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");
				rd.forward(req, resp);
			} else {
				session.setAttribute("inquiry", inquiry);
				req.setAttribute("name", name);
				req.setAttribute("employeeId", employeeId);
				req.setAttribute("genre", genre);
				req.setAttribute("content", content);
				RequestDispatcher rd = req.getRequestDispatcher("/jsp/confirm.jsp");
				rd.forward(req, resp);
			}
		}
	}

	private void generateCaptcha(HttpServletRequest req) {
		Random rand = new Random();
		int num1 = rand.nextInt(10) + 1;
		int num2 = rand.nextInt(10) + 1;
		int answer = num1 + num2;
		req.getSession().setAttribute("captchaAnswer", answer);
		req.setAttribute("captchaQuestion", num1 + " + " + num2 + " = ?");
	}

	private boolean verifyCaptcha(HttpServletRequest req, String userAnswerStr) {
		HttpSession session = req.getSession(false);
		if (session == null)
			return false;
		Integer captchaAnswer = (Integer) session.getAttribute("captchaAnswer");
		if (captchaAnswer == null || userAnswerStr == null || userAnswerStr.isEmpty()) {
			return false;
		}
		try {
			int userAnswer = Integer.parseInt(userAnswerStr);
			return captchaAnswer.equals(userAnswer);
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
