/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Blog   : http://springinpractice.com/
 * GitHub : https://github.com/organizations/springinpractice
 * Book   : http://manning.com/wheeler/
 * Forum  : http://www.manning-sandbox.com/forum.jspa?forumID=503
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
