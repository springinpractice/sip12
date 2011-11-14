/* 
 * Book web site   - http://www.manning.com/wheeler/
 * Book blog       - http://springinpractice.com/
 * Author web site - http://wheelersoftware.com/
 */
package com.springinpractice.ch12.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springinpractice.ch12.model.Article;

/**
 * Article data access object.
 * 
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface ArticleDao extends MongoRepository<Article, String> { }
