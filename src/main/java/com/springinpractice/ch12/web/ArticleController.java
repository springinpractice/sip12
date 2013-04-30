/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Blog   : http://springinpractice.com/
 * GitHub : https://github.com/organizations/springinpractice
 * Book   : http://manning.com/wheeler/
 * Forum  : http://www.manning-sandbox.com/forum.jspa?forumID=503
 */
package com.springinpractice.ch12.web;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springinpractice.ch12.dao.ArticleDao;
import com.springinpractice.ch12.model.Article;
import com.springinpractice.ch12.model.Page;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
@RequestMapping("/articles")
public class ArticleController {
	private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
	
	@Inject private ArticleConverter articleConverter;
	@Inject private ArticleDao articleDao;
	
	/**
	 * @param file multipart file
	 * @return logical view name
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String createArticle(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return "redirect:/articles.html?upload=fail";
		}
		
		Article article = articleConverter.convert(file);
		log.debug("Creating article: {}", article);
		
		articleDao.save(article);
		return "redirect:/articles.html?upload=ok";
	}
	
	/**
	 * @param model model
	 * @return article list
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getArticleList(Model model) {
		log.debug("Getting article list");
		model.addAttribute(articleDao.findAll());
		return getFullViewName("articleList");
	}
	
	/**
	 * @param id article ID
	 * @param pageNumber page number
	 * @param req request
	 * @param res response
	 * @return logical view name
	 * @throws IOException if there's an I/O exception
	 */
	@RequestMapping(value = "/{id}/{page}", method = RequestMethod.GET)
	public String getArticlePage(@PathVariable String id, @PathVariable("page") Integer pageNumber, Model model) {
		log.debug("Serving {}, page {}", id, pageNumber);
		Article article = articleDao.findOne(id);
		Page page = article.getPages().get(pageNumber - 1);
		model.addAttribute(article);
		model.addAttribute("articlePage", page);
		model.addAttribute("pageNumber", pageNumber);
		return getFullViewName("articlePage");
	}
	
	private String getFullViewName(String viewName) {
		return "article/" + viewName;
	}
}
