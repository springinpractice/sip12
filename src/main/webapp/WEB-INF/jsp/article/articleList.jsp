<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jsp/urls.jspf" %>

<c:url var="uploadUrl" value="/articles" />

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Articles</title>
		<link rel="stylesheet" type="text/css" href="${articlesCssUrl}" />
		<script type="text/javascript">
			$(function() {
				$("#uploadLink").click(function() {
					$("#uploadForm")[0].reset();
					$("#uploadFormDiv").show();
				});
				$("#cancelUploadButton").click(function() {
					$("#uploadFormDiv").hide();
				});
			});
		</script>
	</head>
	<body>
		<h1>Articles</h1>
		
		<c:if test="${param.upload == 'fail'}">
			<div class="error alert">The upload failed.</div>
		</c:if>
		
		<c:if test="${param.upload == 'ok'}">
			<div class="info alert">Uploaded.</div>
		</c:if>
		
		<div style="margin: 20px 0">
			<div><span class="add icon"><a id="uploadLink" href="#">Upload article</a></span></div>
			<div id="uploadFormDiv" class="panel" style="display:none;padding:10px 20px">
				<form id="uploadForm" action="${uploadUrl}" method="post" enctype="multipart/form-data">
					<span><input id="fileInput" name="file" type="file" size="48"></input></span>
					<span style="margin-left:10px"><input id="uploadButton" type="submit" value="Upload"></input></span>
					<span style="margin-left:5px"><input id="cancelUploadButton" type="button" value="Cancel"></input></span>
				</form>
			</div>
		</div>
		
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
