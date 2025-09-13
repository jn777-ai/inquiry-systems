package com.example.inquiry;

import java.sql.Timestamp;

public class Inquiry {
	private int id;//DB用のid
	private String name;//ニックネーム
	private String employeeId;//社員番号
	private String genre;//フロア、コンセッションなどのジャンル
	private String content;//内容
	private String status;//一覧表示で使うステータス
	private Timestamp createdAt;//登録日時

	public Inquiry() {
		this.status = "新規";
	}//デフォルトで新規になるように設定

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}
