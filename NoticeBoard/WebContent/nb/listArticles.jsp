<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<c:set var="articleList" value="${articleMap.articleList }"/>
<c:set var="toArticles" value="${articleMap.toArticles }"/>
<c:set var="section" value="${articleMap.section }"/>
<c:set var="pageNum" value="${articleMap.pageNum }"/>

<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
<style>
	.no_uline {
		text-decoration: none;
	}
	.sel_page {
		text-decoration: none;
		color: red;
	}
	.cls1 {
		text-decoration: none;
	}
	.cls2 {
		text-align: center;
		font-size: 30px;
	}
	table {
		text-align: center;
		border: 1;
		width: 80%;
	}
	.h10 {
		height: 10px;
	}
	.tac {
		text-align: center;
	}
	.tal {
		text-align: left;
	}
</style>
</head>
<body>
<table>
<tr class="h10 tac" bgcolor="lightgreen">
	<td>글번호</td>
	<td>작성자</td>
	<td>제목</td>
	<td>작성일</td>
</tr>
<c:choose>
	<c:when test="${articleList==null }">
		<tr class="h10">
			<td colspan="4">
				<p class="tac"><b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b></p>
			</td>
		</tr>
	</c:when>
	<c:when test="${articleList!=null }">
		<c:forEach var="article" items="${articleList }" varStatus="articleNum">
			<tr class="tac">
				<td width="5%">${articleNum.count }</td>
				<td width="10%">${article.id }</td>
				<td class="tal" width="35%">
					<span style="padding-right:30px"></span>
					<c:choose>
						 <c:when test="${article.level > 1 }">
						 	<c:forEach begin="1" end="${article.level }" step="1">
						 		<span style="padding-left:10px"></span>
						 	</c:forEach>
						 	<span style="font-size:12px">[답변]</span>
						 	<a href="${contextPath }/board/viewArticle.do?articleNo=${article.articleNo}" class="cls1">
						 		${article.title }
						 	</a>
						 </c:when>
						 <c:otherwise>
						 	<a href="${contextPath }/board/viewArticle.do?articleNo=${article.articleNo}" class="cls1">
						 		${article.title }
						 	</a>
						 </c:otherwise>
					</c:choose>
				</td>
				<td width="10%">
					<fmt:formatDate value="${article.writeDate }"/>
				</td>
			</tr>
		</c:forEach>
	</c:when>
</c:choose>
</table>
<div class="cls2">
<c:if test="${toArticles!=null }">
	<c:choose>
		<c:when test="${toArticles >100 }">
			<c:forEach var="page" begin="1" end="10" step="1">
				<c:if test="${section>1&&page==1 }">
					<a href="${contextPath }/board/listArticles.do?section=${section-1 }
					&pageNum=${(section-1)*10+1 }" class="no_uline">&nbsp;pre</a>
				</c:if>
				<a href="${contextPath }/board/listArticles.do?section=${section }
					&pageNum=${page }" class="no_uline">${(section-1)*10+page }</a>
				<c:if test="${page==10 }">
					<a href="${contextPath }/board/listArticles.do?section=${section+1 }
					&pageNum=${section*10+1 }" class="no_uline">&nbsp;next</a>
				</c:if>
			</c:forEach>
		</c:when>
		<c:when test="${toArticles==100 }">
			<c:forEach var="page" begin="1" end="10" step="1">
				<a href="#" class="no_uline">${page }</a>
			</c:forEach>
		</c:when>
		
		<c:when test="${toArticles<100 }">
			<c:forEach var="page" begin="1" end="${toArticles/10+1 }" step="1">
				<c:choose>
					<c:when test="${page==pageNum }">
						<a href="${contextPath }/board/listArticles.do?section=
						${section }&pageNum=${page }" class="sel_page">${page }</a>
					</c:when>
					<c:otherwise>
						<a href="${contextPath }/board/listArticles.do?section=
						${section }&pageNum=${page }" class="no_uline">${page }</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:when>
	</c:choose>
</c:if>
</div>
<p class="cls2"><a href="${contextPath }/board/articleForm.do" class="cls1">글쓰기</a></p>
</body>
</html>