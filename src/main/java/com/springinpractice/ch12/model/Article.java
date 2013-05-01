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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@XmlRootElement(name = "article")
public class Article {
	private String id;
	private String title;
	private String author;
	private Date publishDate;
	private String description;
	private String keywords;
	private List<Page> pages = new ArrayList<Page>();
	
	@XmlAttribute
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
	
	@XmlTransient
	public List<Page> getPages() { return pages; }
	
	public void setPages(List<Page> pages) { this.pages = pages; }
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Article: id=");
		builder.append(id);
		builder.append(", author=");
		builder.append(author);
		builder.append(", publishDate=");
		builder.append(publishDate);
		builder.append(", description=");
		builder.append(description);
		builder.append(", keywords=");
		builder.append(keywords);
		builder.append(", numPages=");
		builder.append(pages == null ? 0 : pages.size());
		builder.append("]");
		return builder.toString();
	}
}
