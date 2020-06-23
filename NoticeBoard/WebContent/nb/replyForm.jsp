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
<title>답글쓰기</title>
<style>
.tac {
	text-align: center;
}
.tar {
	text-align: right;
}
.tal {
	text-align: left;
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
<h1 class="tac">답글쓰기</h1>
<form action="${contextPath }/board/addReply.do" name="frmReply" method="post"
enctype="multipart/form-data">
<table class="tac">
	<tr>
		<td class="tar">글쓴이 : &nbsp;</td>
		<td><input type="text" size="5" value="lee" disabled/></td>
	</tr>
	<tr>
		<td class="tar">글제목 : &nbsp;</td>
		<td><input type="text" size="67" maxlength="100" name="title"/></td>
	</tr>
	<tr>
		<td class="tar" valign="top">글내용 : &nbsp;</td>
		<td><textarea name="content" cols="65" rows="10" maxlength="4000"></textarea></td>
	</tr>
	<tr>
		<td class="tar">이미지파일 첨부 : </td>
		<td><input type="file" name="imageFileName" onchange="readURL(this);"/></td>
		<td><img src="#" width="200" height="200" id="preview" /></td>
	</tr>
	<tr>
		<td class="tar"></td>
		<td>
			<input type="submit" value="답글반영하기" />
			<input type="button" value="취소" onclick="backToList(this.form)"/>
		</td>
	</tr>
</table>	
</form>
</body>
</html>