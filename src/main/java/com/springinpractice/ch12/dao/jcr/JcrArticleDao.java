/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch12.dao.jcr;

import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.support.JcrDaoSupport;

import com.springinpractice.ch12.dao.ArticleDao;
import com.springinpractice.ch12.model.Article;
import com.springinpractice.ch12.model.Page;

/**
 * JCR-backed article data access object.
 * 
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Repository
@Transactional(readOnly = true)
public class JcrArticleDao extends JcrDaoSupport implements ArticleDao {
	@Inject private ArticleMapper articleMapper;
	
	/* (non-Javadoc)
	 * @see org.springmodules.jcr.support.JcrDaoSupport#setSessionFactory(org.springmodules.jcr.SessionFactory)
	 */
	@Inject
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.ch12.dao.ArticleDao#createOrUpdate(com.springinpractice.ch12.model.Article)
	 */
	@Override
	@Transactional(readOnly = false)
	public void createOrUpdate(final Article article) {
		notNull(article);
		getTemplate().execute(new JcrCallback() {

			/* (non-Javadoc)
			 * @see org.springmodules.jcr.JcrCallback#doInJcr(javax.jcr.Session)
			 */
			@Override
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				if (exists(article.getId())) { delete(article); }
				create(article);
				return null;
			}
		}, true);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch12.dao.ArticleDao#create(com.springinpractice.ch12.model.Article)
	 */
	@Override
	@Transactional(readOnly = false)
	public void create(final Article article) {
		notNull(article);
		getTemplate().execute(new JcrCallback() {

			/* (non-Javadoc)
			 * @see org.springmodules.jcr.JcrCallback#doInJcr(javax.jcr.Session)
			 */
			@Override
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				if (exists(article.getId())) {
					throw new DataIntegrityViolationException("Article already exists");
				}
				articleMapper.addArticleNode(article, getArticlesNode(session));
				session.save();
				return null;
			}
		}, true);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#getAll()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Article> getAll() {
		return (List<Article>) getTemplate().execute(new JcrCallback() {

			/* (non-Javadoc)
			 * @see org.springmodules.jcr.JcrCallback#doInJcr(javax.jcr.Session)
			 */
			@Override
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				NodeIterator it = getArticlesNode(session).getNodes();
				List<Article> articles = new ArrayList<Article>();
				while (it.hasNext()) { articles.add(articleMapper.toArticle(it.nextNode())); }
				return articles;
			}
		}, true);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#get(java.io.Serializable)
	 */
	@Override
	public Article get(Serializable id) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#load(java.io.Serializable)
	 */
	@Override
	public Article load(Serializable id) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch12.dao.ArticleDao#getPage(java.lang.String, int)
	 */
	@Override
	public Article getPage(final String articleId, final int pageNumber) {
		notNull(articleId);
		return (Article) getTemplate().execute(new JcrCallback() {

			/* (non-Javadoc)
			 * @see org.springmodules.jcr.JcrCallback#doInJcr(javax.jcr.Session)
			 */
			@Override
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				Node articlesNode = getArticlesNode(session);
				Node articleNode = articlesNode.getNode(articleId);
				
				if (articleNode == null) {
					throw new DataRetrievalFailureException("No such article: " + articleId);
				}
				
				Node pagesNode = articleNode.getNode("pages");
				
				Article article = articleMapper.toArticle(articleNode);
				List<Page> pages = new ArrayList<Page>();
				NodeIterator pagesIt = pagesNode.getNodes();
				int count = 1;
				while (pagesIt.hasNext()) {
					Node pageNode = pagesIt.nextNode();
					Page page = new Page();
					if (count == pageNumber) {
						Node contentNode = pageNode.getNode(Node.JCR_CONTENT);
						page.setContent(contentNode.getProperty("jcr:data").getString());
					}
					pages.add(page);
					count++;
				}
				article.setPages(pages);
				return article;
			}
		}, true);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch12.dao.ArticleDao#update(com.springinpractice.ch12.model.Article)
	 */
	@Override
	@Transactional(readOnly = false)
	public void update(Article article) {
		throw new UnsupportedOperationException("Not implemented");
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#delete(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Article article) { deleteById(article.getId()); }

	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#deleteById(java.io.Serializable)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteById(final Serializable id) {
		notNull(id);
		getTemplate().execute(new JcrCallback() {

			/* (non-Javadoc)
			 * @see org.springmodules.jcr.JcrCallback#doInJcr(javax.jcr.Session)
			 */
			@Override
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				getArticlesNode(session).getNode((String) id).remove();
				session.save();
				return null;
			}
		}, true);
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#deleteAll()
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteAll() {
		throw new UnsupportedOperationException("Not implemented");
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#count()
	 */
	@Override
	public long count() {
		throw new UnsupportedOperationException("Not implemented");
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.ch12.dao.ArticleDao#exists(java.lang.String)
	 */
	@Override
	public boolean exists(Serializable id) {
		notNull(id);
		return getTemplate().itemExists(getArticlePath((String) id));
	}
	
	
	// =================================================================================================================
	// Helpers
	// =================================================================================================================
	
	private String getArticlesNodeName() {
		return "articles";
	}
	
	private String getArticlesPath() {
		return "/" + getArticlesNodeName();
	}
	
	private String getArticlePath(String articleId) {
		return getArticlesPath() + "/" + articleId;
	}
	
	private Node getArticlesNode(Session session) throws RepositoryException {
		try {
			return session.getNode(getArticlesPath());
		} catch (PathNotFoundException e) {
			return session.getRootNode().addNode(getArticlesNodeName());
		}
	}
}
