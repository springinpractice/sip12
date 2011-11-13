<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/urls.jspf" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Articles</title>
		<link rel="stylesheet" type="text/css" href="${articlesCssUrl}" />
	</head>
	<body>
		<h1>Articles</h1>
		<c:choose>
			<c:when test="${empty articleList}">
				<p>None</p>
			</c:when>
			<c:otherwise>
				<div id="articleList">
					<c:forEach var="article" items="${articleList}">
						<c:url var="articleUrl" value="/articles/${article.id}/1.html" />
						<div class="articleItem">
							<div class="articleTitle"><a href="${articleUrl}"><c:out value="${article.title}" /></a></div>
							<c:if test="${not empty article.author}">
								<div class="articleAuthor"><c:out value="${article.author}" /></div>
							</c:if>
							<c:if test="${not empty article.publishDate}">
								<div class="articlePublishDate"><span class="date icon"><fmt:formatDate value="${article.publishDate}" /></span></div>
							</c:if>
							<c:if test="${not empty article.description}">
								<div class="articleDescription"><c:out value="${article.description}" /></div>
							</c:if>
						</div>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
	</body>
</html>
