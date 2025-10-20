package com.example.inquiry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InquiryDAO {

	private final String url = "jdbc:postgresql://localhost:5432/postgres";
	private final String user = "postgres";
	private final String password = "postgres";

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addInquiry(Inquiry inquiry) throws SQLException {//DBに情報を追加するメソッド
		String sql = "INSERT INTO inquiries (name, employee_id, genre, content, status) VALUES (?, ?, ?, ?, ?)";
		try (Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, inquiry.getName());
			ps.setString(2, inquiry.getEmployeeId());
			ps.setString(3, inquiry.getGenre());
			ps.setString(4, inquiry.getContent());
			ps.setString(5, inquiry.getStatus() != null ? inquiry.getStatus() : "未対応");//nullなら初期値で"未対応"に設定
			ps.executeUpdate();
		}
	}

	public List<Inquiry> getAllInquiries() throws SQLException {//データを全てsqlに代入してwhileでlistに入れて戻り値に設定
		List<Inquiry> list = new ArrayList<>();
		String sql = "SELECT * FROM inquiries ORDER BY created_at DESC";
		try (Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Inquiry i = mapResultSetToInquiry(rs);
				list.add(i);
			}
		}
		return list;
	}

	public Inquiry getInquiryById(int id) throws SQLException {//idから一件を取得
		String sql = "SELECT * FROM inquiries WHERE id = ?";
		try (Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToInquiry(rs);
				}
			}
		}
		return null;
	}

	public boolean updateInquiryStatus(int id, String newStatus) throws SQLException {//ステータスを更新
		String sql = "UPDATE inquiries SET status = ? WHERE id = ?";
		try (Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, newStatus); //一つ目のスペースホルダにnewStatusを代入
			ps.setInt(2, id);//二つ目のスペースホルダにidを代入
			int updated = ps.executeUpdate();
			return updated > 0;//1ならtrueで返す
		}
	}

	public List<Inquiry> getInquiriesByGenreAndStatus(String genre, String status) throws SQLException {//ジャンルステータス検索メソッド
		List<Inquiry> list = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM inquiries WHERE 1=1");
		if (genre != null && !genre.isEmpty()) {//ジャンルがnullか空ではないなら条件に追加å
			sql.append(" AND genre = ?");
		}
		if (status != null && !status.isEmpty()) {//上のステータス版
			sql.append(" AND status = ?");
		}
		sql.append(" ORDER BY created_at DESC");//登録日時が新しい順へ変更

		try (Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = con.prepareStatement(sql.toString())) {
			int index = 1;
			if (genre != null && !genre.isEmpty()) {//もし指定があるなら(1,genre)
				ps.setString(index++, genre);
			}
			if (status != null && !status.isEmpty()) {//もし指定があるなら(2,status)
				ps.setString(index++, status);
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapResultSetToInquiry(rs));
				}
			}
		}
		return list;
	}

	public boolean deleteInquiryById(int id) throws SQLException {//削除メソッド
		String sql = "DELETE FROM inquiries WHERE id = ?";
		try (Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, id);//プレースホルダに対象のidをセット
			int deleted = ps.executeUpdate(); //削除された行数がdeletedに入る
			return deleted > 0;//更新と同じで1ならtrue(削除成功)
		}
	}

	private Inquiry mapResultSetToInquiry(ResultSet rs) throws SQLException {
		Inquiry i = new Inquiry();
		i.setId(rs.getInt("id"));
		i.setName(rs.getString("name"));
		i.setEmployeeId(rs.getString("employee_id"));
		i.setGenre(rs.getString("genre"));
		i.setContent(rs.getString("content"));
		i.setStatus(rs.getString("status"));
		i.setCreatedAt(rs.getTimestamp("created_at"));
		return i;
	}
}
