<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${fn:length(article.pages) > 1}">
	<div class="pageNavigation">
		<ul>
			<c:choose>
				<c:when test="${pageNumber == 1}">
					<li><a class="disabled">&laquo;</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${pageNumber - 1}.html">&laquo;</a></li>
				</c:otherwise>
			</c:choose>
			<c:forEach var="page" items="${article.pages}" varStatus="status">
				<c:set var="curr" value="${status.index + 1}" />
				<c:choose>
					<c:when test="${curr == pageNumber}">
						<li><a href="${curr}.html" class="currentPage">${curr}</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${curr}.html">${curr}</a></li>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${pageNumber < fn:length(article.pages)}">
					<li><a href="${pageNumber + 1}.html">&raquo;</a></li>
				</c:when>
				<c:otherwise>
					<a class="disabled">&raquo;</a>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</c:if>
