package com.eddicorp.model;
/**
 * @since       2023.02.28
 * @author      martin
 * @description posts
 **********************************************************************************************************************/
public class Posts {

	private String title;
	private String author;
	private String content;

	public Posts() {
	}

	public Posts(String title, String author, String content) {
		this.title=title;
		this.author=author;
		this.content=content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author=author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content=content;
	}
}
