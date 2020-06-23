<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
<style>
	table {
		border: 0;
	}
	.tac {
		text-align: center;
	}
	.tar {
		text-align: right;
	}
	.col2 {
		width: 600px;
	}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script type="text/javascript">
	function readURL(input) {
		if(input.files && input.files[0]) {
			var reader=new FileReader();
			reader.onload=function(e) {
				$('#preview').attr('src', e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	function backToList(obj) {
		obj.action="${contextPath}/board/listArticles.do";
		obj.submit();
	}
</script>
</head>
<body>
	<h1 class="tac">게시글 쓰기</h1>
	<form action="${contextPath }/board/addArticle.do" name="articleForm" method="post" enctype="multipart/form-data">
		<table class="tac">
			<tr>
				<td class="tar">글제목 : </td>
				<td class="col2" colspan="2"><input type="text" name="title" size="67" maxlength="500" /></td>
			</tr>
			<tr>
				<td class="tar" valign="top"><br />글내용 : </td>
				<td class="col2" colspan="2"><textarea name="content" cols="65" rows="10" maxlength="4000"></textarea></td>
			</tr>
			<tr>
				<td class="tar">이미지 : </td>
				<td><input type="file" name="imageFileName" id="" onchange="readURL(this)" /></td>
				<td><img src="#" id="preview" width="200" height="200" /></td>
			</tr>
			<tr>
				<td class="tar"></td>
				<td class="col2" colspan="2">
					<input type="submit" value="글쓰기" />
					<input type="button" value="목록보기" onClick="backToList(this.form)" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>