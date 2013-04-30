/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Blog   : http://springinpractice.com/
 * GitHub : https://github.com/organizations/springinpractice
 * Book   : http://manning.com/wheeler/
 * Forum  : http://www.manning-sandbox.com/forum.jspa?forumID=503
 */
package com.springinpractice.ch12.web;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.springinpractice.ch12.model.Article;
import com.springinpractice.ch12.model.Page;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Component
public class ArticleConverter implements Converter<MultipartFile, Article>, ServletContextAware {
	private static final int BUFFER_SIZE = 4096;
	private static final Logger log = LoggerFactory.getLogger(ArticleConverter.class);
	
	@Inject private Unmarshaller unmarshaller;
	
	private ServletContext servletContext;
	private Random random = new Random();
	
	/* (non-Javadoc)
	 * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Article convert(MultipartFile zipFile) {
		try {
			File tempDir = createTempDir();
			unzip(zipFile, tempDir);
			Article article = assembleArticle(tempDir);
			tempDir.delete();
			return article;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private File createTempDir() {
		File tempDir = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		File articleDir;
		int count = 0;
		while ((articleDir = new File(tempDir, "article-" + Math.abs(random.nextLong()))).exists()) {
			if (count++ > 5) {
				throw new RuntimeException("Can't create a temporary directory. Something is very wrong.");
			}
		}
		articleDir.mkdirs();
		return articleDir;
	}
	
	private void unzip(MultipartFile zipFile, File destDir) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipFile.getBytes()));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			File entryFile = new File(destDir, entry.getName());
			log.debug("Extracting {} to {}", entry, entryFile);
			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(entryFile), BUFFER_SIZE);
			int len;
			while ((len = zis.read(buffer)) > 0) { bos.write(buffer, 0, len); }
			bos.flush();
			bos.close();
		}
		
		zis.close();
	}
	
	private Article assembleArticle(File articleDir) throws IOException {
		
		// Read the article
		File articleFile = new File(articleDir, "article.xml");
		StreamSource articleSrc = new StreamSource(articleFile);
		Article article = (Article) unmarshaller.unmarshal(articleSrc);
		log.debug("Unmarshalled article: id={}, title={}", article.getId(), article.getTitle());
		
		// Read the pages
		List<Page> pages = article.getPages();
		File pageFile;
		int pageNumber = 1;
		while ((pageFile = new File(articleDir, pageNumber + ".html")).exists()) {
			
			// Read the file
			StringBuilder builder = new StringBuilder(4096);
			BufferedReader br = new BufferedReader(new FileReader(pageFile));
			String line;
			while ((line = br.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
			
			// Extract the content
			String htmlPage = builder.toString();
			int startIndex = htmlPage.indexOf("<body>") + 6;
			int endIndex = htmlPage.indexOf("</body>");
			
			if (startIndex == -1 || endIndex == -1) {
				// FIXME Choose a better exception type for this.
				throw new RuntimeException("Invalid HTML page: " + pageFile + " must have <body> and </body> tags.");
			}
			
			String content = htmlPage.substring(startIndex, endIndex);
			Page page = new Page();
			page.setContent(content);
			pages.add(page);
			
			pageNumber++;
		}
		
		return article;
	}
}
