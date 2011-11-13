/* 
 * Book web site   - http://www.manning.com/wheeler/
 * Book blog       - http://springinpractice.com/
 * Author web site - http://wheelersoftware.com/
 */
package com.springinpractice.ch12.dao.jcr;

import static org.springframework.util.Assert.notNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.support.JcrDaoSupport;

import com.springinpractice.ch12.dao.ArticleDao;
import com.springinpractice.ch12.model.Article;
import com.springinpractice.ch12.model.Page;

/**
 * <p>
 * Article data access object.
 * </p>
 * <p>
 * This class assumes that article IDs are globally unique. This might make it unsuitable for certain applications.
 * </p>
 * 
 * @version $Id$
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Repository
@Transactional(readOnly = true)
public class JcrArticleDao extends JcrDaoSupport implements ArticleDao {
	private static final Logger log = LoggerFactory.getLogger(JcrArticleDao.class);
	
	@Inject private ArticleMapper articleMapper;
	
	@Inject
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.ch12.dao.ArticleDao#createOrUpdate(com.springinpractice.ch12.model.Article)
	 */
	@Override
	@Transactional(readOnly = false)
	public void createOrUpdate(Article article) {
		notNull(article);
		log.debug("Creating or updating article: {}", article);
		if (exists(article.getId())) { delete(article); }
		create(article);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch12.dao.ArticleDao#create(com.springinpractice.ch12.model.Article)
	 */
	@Override
	@Transactional(readOnly = false)
	public void create(Article article) {
		notNull(article);
		String articleId = article.getId();
		if (exists(articleId)) {
			throw new DataIntegrityViolationException("Article already exists");
		}
		try {
			articleMapper.addArticleNode(article, getArticlesNode());
			getSession().save();
		} catch (RepositoryException e) {
			throw convertJcrAccessException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#getAll()
	 */
	@Override
	public List<Article> getAll() {
		try {
			Node articlesNode = getArticlesNode();
			NodeIterator it = articlesNode.getNodes();
			List<Article> articles = new ArrayList<Article>();
			while (it.hasNext()) {
				articles.add(articleMapper.toArticle(it.nextNode()));
			}
			return articles;
		} catch (RepositoryException e) {
			throw convertJcrAccessException(e);
		}
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
	public Article getPage(String articleId, int pageNumber) {
		notNull(articleId);
		try {
			Node articlesNode = getArticlesNode();
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
		} catch (RepositoryException e) {
			throw convertJcrAccessException(e);
		}
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
	public void delete(Article article) {
		deleteById(article.getId());
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.dao.Dao#deleteById(java.io.Serializable)
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteById(Serializable id) {
		notNull(id);
		String articleId = (String) id;
		try {
			getArticlesNode().getNode(articleId).remove();
			getSession().save();
		} catch (RepositoryException e) {
			throw convertJcrAccessException(e);
		}
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
		String articleId = (String) id;
		try {
			return getSession().nodeExists(getArticlePath(articleId));
		} catch (RepositoryException e) {
			throw convertJcrAccessException(e);
		}
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
	
	private Node getArticlesNode() throws RepositoryException {
		Session session = getSession();
		try {
			return session.getNode(getArticlesPath());
		} catch (PathNotFoundException e) {
			return session.getRootNode().addNode(getArticlesNodeName());
		}
	}
}
