/* 
 * Book web site   - http://www.manning.com/wheeler/
 * Book blog       - http://springinpractice.com/
 * Author web site - http://wheelersoftware.com/
 */
package com.springinpractice.ch12.dao;

import com.springinpractice.ch12.model.Article;
import com.springinpractice.dao.Dao;

/**
 * Article data access object.
 * 
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface ArticleDao extends Dao<Article> {
	
	/**
	 * @param article article to create or update
	 */
	void createOrUpdate(Article article);
	
	/**
	 * Returns the article with the given page loaded.
	 * 
	 * @param articleId article ID
	 * @param pageNumber page number
	 * @return article with the given page loaded
	 */
	Article getPage(String articleId, int pageNumber);
}
