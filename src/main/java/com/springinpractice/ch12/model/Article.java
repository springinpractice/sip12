/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch12.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public class Article {
	private String id;
	private String title;
	private String author;
	private Date publishDate;
	private String description;
	private String keywords;
	private List<Page> pages = new ArrayList<Page>();
	
	public String getId() { return id; }
	
	public void setId(String id) { this.id = id; }
	
	public String getTitle() { return title; }

	public void setTitle(String title) { this.title = title; }

	public String getAuthor() { return author; }

	public void setAuthor(String author) { this.author = author; }

	public Date getPublishDate() { return publishDate; }

	public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }
	
	public String getDescription() { return description; }
	
	public void setDescription(String description) { this.description = description; }
	
	public String getKeywords() { return keywords; }
	
	public void setKeywords(String keywords) { this.keywords = keywords; }
	
	public List<Page> getPages() { return pages; }
	
	public void setPages(List<Page> pages) { this.pages = pages; }
	
	@Override
	public String toString() {
		return "[Article: id=" + id
			+ ", title=" + title
			+ ", author=" + author
			+ ", publishDate=" + publishDate
			+ ", description=" + description
			+ ", keywords=" + keywords
			+ ", numPages=" + (pages == null ? 0 : pages.size())
			+ "]";
	}
}
