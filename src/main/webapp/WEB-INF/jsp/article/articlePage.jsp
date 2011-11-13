<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="/WEB-INF/jsp/urls.jspf" %>

<%-- Syntax highlighter --%>
<c:set var="baseShPath" value="/syntaxhighlighter" />
<c:url var="shCoreDefaultCssUrl" value="${baseShPath}/styles/shCoreDefault.css" />
<c:url var="shCoreJsUrl" value="${baseShPath}/scripts/shCore.js" />
<c:url var="shBrushJavaJsUrl" value="${baseShPath}/scripts/shBrushJava.js" />
<c:url var="shBrushPlainJsUrl" value="${baseShPath}/scripts/shBrushPlain.js" />
<c:url var="shBrushXmlJsUrl" value="${baseShPath}/scripts/shBrushXml.js" />

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><c:out value="${article.title}" /> - Page <c:out value="${pageNumber}" /></title>
		<link rel="stylesheet" type="text/css" href="${shCoreDefaultCssUrl}" />
		<link rel="stylesheet" type="text/css" href="${articlesCssUrl}" />
		<script type="text/javascript" src="${shCoreJsUrl}"></script>
		<script type="text/javascript" src="${shBrushJavaJsUrl}"></script>
		<script type="text/javascript" src="${shBrushPlainJsUrl}"></script>
		<script type="text/javascript" src="${shBrushXmlJsUrl}"></script>
		<script type="text/javascript">
			$(function() { 	SyntaxHighlighter.all(); });
		</script>
	</head>
	<body>
		<div><jsp:include page="pageNav.jsp" /></div>
		
		<c:choose>
			<c:when test="${pageNumber == 1}">
				<h1 id="articleTitle"><c:out value="${article.title}" /></h1>
				<div style="margin:20px 0">
					<c:if test="${not empty article.author}">
						<div id="byline"><c:out value="${article.author}" /></div>
					</c:if>
					<c:if test="${not empty article.publishDate}">
						<div id="dateline"><span class="date icon"><fmt:formatDate value="${article.publishDate}" /></span></div>
					</c:if>
				</div>
				<div id="deck">
					<c:out value="${article.description}" />
				</div>
			</c:when>
			<c:otherwise>
				<h4><c:out value="${article.title}" /></h4>
			</c:otherwise>
		</c:choose>
		
		<c:out value="${articlePage.content}" escapeXml="false" />
		
		<div><jsp:include page="pageNav.jsp" /></div>
	</body>
</html>
