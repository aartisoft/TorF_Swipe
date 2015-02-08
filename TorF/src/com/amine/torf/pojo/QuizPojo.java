package com.amine.torf.pojo;

public class QuizPojo {

	int _id;
	String _question;
	int _isTrue;
	int _difficulty;
	String _category;
	String _comment;

	public QuizPojo(int id, String _question, int _isTrue, int _difficulty,
			String category_name, String _comment) {
		super();
		this._id = id;
		this._question = _question;
		this._isTrue = _isTrue;
		this._difficulty = _difficulty;
		this._category = category_name;
		this._comment = _comment;

	}

	// private variables


	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_question() {
		return _question;
	}

	public void set_question(String _question) {
		this._question = _question;
	}

	public int get_isTrue() {
		return _isTrue;
	}

	public void set_isTrue(int _isTrue) {
		this._isTrue = _isTrue;
	}

	public int get_difficulty() {
		return _difficulty;
	}

	public void set_difficulty(int _difficulty) {
		this._difficulty = _difficulty;
	}

	public String get_category() {
		return _category;
	}

	public void set_category(String _category) {
		this._category = _category;
	}

	public String get_comment() {
		return _comment;
	}

	public void set_comment(String _comment) {
		this._comment = _comment;
	}

	// Empty constructor
	public QuizPojo() {

	}
}