/* 
 * Book web site   - http://www.manning.com/wheeler/
 * Book blog       - http://springinpractice.com/
 * Author web site - http://wheelersoftware.com/
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
	
	/**
	 * @return id
	 */
	public String getId() { return id; }
	
	/**
	 * @param id id
	 */
	public void setId(String id) { this.id = id; }
	
	/**
	 * @return the title
	 */
	public String getTitle() { return title; }

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) { this.title = title; }

	/**
	 * @return the author
	 */
	public String getAuthor() { return author; }

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) { this.author = author; }

	/**
	 * @return the publishDate
	 */
	public Date getPublishDate() { return publishDate; }

	/**
	 * @param publishDate
	 *            the publishDate to set
	 */
	public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }
	
	/**
	 * @return description
	 */
	public String getDescription() { return description; }
	
	/**
	 * @param description description
	 */
	public void setDescription(String description) { this.description = description; }
	
	/**
	 * @return keywords
	 */
	public String getKeywords() { return keywords; }
	
	/**
	 * @param keywords keywords
	 */
	public void setKeywords(String keywords) { this.keywords = keywords; }
	
	/**
	 * @return pages
	 */
	public List<Page> getPages() { return pages; }
	
	/**
	 * @param pages pages
	 */
	public void setPages(List<Page> pages) { this.pages = pages; }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
