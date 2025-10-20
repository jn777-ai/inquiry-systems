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

		if ("history".equals(action)) {//actionがhistoryの場合の処理
			HttpSession session = req.getSession(false);//既存のセッションを取得→なければ管理者ではない
			Boolean isAdmin = false;
			if (session != null && session.getAttribute("isAdmin") instanceof Boolean) {//セッションの中にあるisAdminというキーで保存されている値
				isAdmin = (Boolean) session.getAttribute("isAdmin");//isAdminがnullでなく、trueかfalseであることを確認した上で,isAdminに値を代入
			} //管理者)ならtrue 一般(ログイン前も含む)ならfalse

			if (!isAdmin) {//管理者でないならlogin.jspへ遷移
				resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp");
				return;
			}

			String genreFilter = req.getParameter("genre");//ジャンルを取得
			String statusFilter = req.getParameter("status");//ステータスを取得

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

		if ("complete".equals(action)) {//actionがcompleteの時の処理
			Inquiry inquiry = (Inquiry) session.getAttribute("inquiry");//名前や社員番号などが入ったinquiryオブジェクトを取り出してる
			if (inquiry != null) {
				try {//inquiryオブジェクトが存在すればaddInquiryメソッドでDBに登録
					inquiryDAO.addInquiry(inquiry);
				} catch (SQLException e) {//エラーが出た時の処理
					e.printStackTrace();
					req.setAttribute("errorMessage", "DBエラーが発生しました。");
					RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");//error.jspへ遷移
					rd.forward(req, resp);
					return;//中断される
				}
			}
			session.removeAttribute("inquiry");//セッションで使っていたinquiryオブジェクトを削除
			RequestDispatcher rd = req.getRequestDispatcher("/jsp/complete.jsp");//complete.jspへ遷移
			rd.forward(req, resp);

		} else if ("updateStatus".equals(action)) {//ステータス変更時の処理

			String idStr = req.getParameter("id");//変更した投稿のidを取得
			String newStatus = req.getParameter("newStatus");//新たなステータスを取得

			try {
				int id = Integer.parseInt(idStr);//String型なのをint型へ変更
				inquiryDAO.updateInquiryStatus(id, newStatus);//該当するレコードを新たなものに更新
			} catch (NumberFormatException | SQLException e) {//idStrが数字出なかったり、SQLでエラーが起きた時の処理
				e.printStackTrace();
				req.setAttribute("errorMessage", "ステータス更新に失敗しました。");
				RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");//error.jspへ遷移
				rd.forward(req, resp);
				return;//中断
			}

			resp.sendRedirect(req.getContextPath() + "/inquiry?action=history");//更新成功時に問い合わせ履歴へリダイレクト

		} else if ("delete".equals(action)) {//削除の処理

			String idStr = req.getParameter("id");//削除対象のidを代入
			try {
				int id = Integer.parseInt(idStr);//int型へ
				inquiryDAO.deleteInquiryById(id);//削除処理
			} catch (NumberFormatException | SQLException e) {//idStrが数字でなかったりSQLでエラーが起きた場合の処理
				e.printStackTrace();
				req.setAttribute("errorMessage", "削除に失敗しました。");
				RequestDispatcher rd = req.getRequestDispatcher("/jsp/error.jsp");//error.jspへ遷移
				rd.forward(req, resp);
				return;
			}

			resp.sendRedirect(req.getContextPath() + "/inquiry?action=history");//削除成功時に問い合わせ履歴へリダイレクト

		} else {
			String name = req.getParameter("name");
			String employeeId = req.getParameter("employeeId");
			String genre = req.getParameter("genre");
			String content = req.getParameter("content");
			String captchaInput = req.getParameter("captcha");

			Map<String, String> errors = new HashMap<>();//エラーメッセージを入れる

			if (name != null && name.trim().isEmpty()) {//nullならnullで処理
				name = null;
			}

			if (employeeId == null || !employeeId.matches("^\\d{6}$")) {//6桁出ないならerror
				errors.put("employeeId", "社員番号は6桁の数字で入力してください。");
			}

			if (genre == null || genre.trim().isEmpty()) {//nullか未入力ならerror
				errors.put("genre", "ジャンルを選択してください。");
			}

			if (content == null || content.trim().isEmpty()) {//nullか未入力ならerror
				errors.put("content", "内容は必須です。");
			}

			if (!verifyCaptcha(req, captchaInput)) {
				errors.put("captcha", "CAPTCHA が不正です。");
			}

			Inquiry inquiry = new Inquiry();//inquiryオブジェクトを作成
			inquiry.setName(name);
			inquiry.setEmployeeId(employeeId);
			inquiry.setGenre(genre);
			inquiry.setContent(content);
			inquiry.setStatus("新規");

			if (!errors.isEmpty()) {//エラー時
				req.setAttribute("errors", errors);//errorsを参照
				req.setAttribute("inquiry", inquiry);//エラー以外の項目を引き継ぎ
				generateCaptcha(req);
				RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");//トップページへ
				rd.forward(req, resp);
			} else {//正常時
				session.setAttribute("inquiry", inquiry);
				req.setAttribute("name", name);
				req.setAttribute("employeeId", employeeId);
				req.setAttribute("genre", genre);
				req.setAttribute("content", content);
				RequestDispatcher rd = req.getRequestDispatcher("/jsp/confirm.jsp");//完了確認ページへ
				rd.forward(req, resp);
			}
		}
	}

	private void generateCaptcha(HttpServletRequest req) {//CAPTCHAの作成
		Random rand = new Random();
		int num1 = rand.nextInt(10) + 1;
		int num2 = rand.nextInt(10) + 1;
		int answer = num1 + num2;
		req.getSession().setAttribute("captchaAnswer", answer);
		req.setAttribute("captchaQuestion", num1 + " + " + num2 + " = ?");
	}

	private boolean verifyCaptcha(HttpServletRequest req, String userAnswerStr) {
		HttpSession session = req.getSession(false);//セッションを取得
		if (session == null)//空の場合falseを返す
			return false;
		Integer captchaAnswer = (Integer) session.getAttribute("captchaAnswer");//セッションにある答えを所得
		if (captchaAnswer == null || userAnswerStr == null || userAnswerStr.isEmpty()) {//セッションの答えがnull、ユーザの答えがnull,ユーザの答えが未入力の場合falseを返す
			return false;
		}
		try {//ユーザの答えをint型にしてcaptchaAnswerと一致していたらtrue
			int userAnswer = Integer.parseInt(userAnswerStr);
			return captchaAnswer.equals(userAnswer);
		} catch (NumberFormatException e) {//違う場合はここにきてfalseを返す
			return false;
		}
	}
}
