/* 
 * Book web site   - http://www.manning.com/wheeler/
 * Book blog       - http://springinpractice.com/
 * Author web site - http://wheelersoftware.com/
 */
package com.springinpractice.ch12.web;

import static org.springframework.util.Assert.isTrue;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	@Inject private ArticleDao articleDao;
	
	/**
	 * @param id article ID
	 * @param article article
	 * @return logical view name
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String importArticle(@PathVariable("id") String id, @RequestBody Article article) {
		log.debug("Importing article: {}", id);
		isTrue(id.equals(article.getId()));
		articleDao.createOrUpdate(article);
		
		// FIXME With the Flex client this needed an HttpServletResponse param. Does it still?
		
		return null;
	}
	
	/**
	 * @param model model
	 * @return article list
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getArticleList(Model model) {
		log.debug("Getting article list");
		model.addAttribute(articleDao.getAll());
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
		Article article = articleDao.getPage(id, pageNumber);
		Page page = article.getPages().get(pageNumber - 1);
		model.addAttribute(article);
		
		// FIXME Temporary hack; handle this on import
		String content = page.getContent();
		int start = content.indexOf("<body>");
		int end = content.indexOf("</body>");
		page.setContent(content.substring(start + 6, end));
		
		model.addAttribute("articlePage", page);
		model.addAttribute("pageNumber", pageNumber);
		return getFullViewName("articlePage");
	}
	
	private String getFullViewName(String viewName) {
		return "article/" + viewName;
	}
}
