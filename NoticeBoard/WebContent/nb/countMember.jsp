<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<c:set var="memberList" value="${memberList }"/>
<% request.setCharacterEncoding("utf-8"); %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 갯수</title>
<style>
 table {
 	text-align: center;
 	border: 2px;
 }
 tr {
 	text-align: center;
 	border: 2px];
 }
</style>
</head>
<body>
<table>
	<tr>
		<td>아이디</td>
		<td>게시글 갯수</td>
	</tr>
<c:choose>
	<c:when test="${memberList==null }">
		<tr>
			<td colspan="2">
				<p><b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b></p>
			</td>
		</tr>
	</c:when>
	<c:when test="${memberList!=null }">
		<c:forEach var="member" items="${memberList }">
			<tr class="tac">
				<td width="5%">${member.id }</td>
				<td width="10%">${member.count }</td>
			</tr>
		</c:forEach>
	</c:when>
</c:choose>		
</table>
</body>
</html>