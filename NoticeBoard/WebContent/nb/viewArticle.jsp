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
<title>Insert title here</title>
<style>
.col1 {
	text-align: center;
	background-color: #ff9933;
	width: 150;
}
.col2 {
	text-align: center;
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
	function fn_enable(obj) {
		document.getElementById("tr_btn").style.display="none";
		document.getElementById("tr_btn_modify").style.display="block";
		document.getElementById("i_title").disabled=false;
		document.getElementById("i_content").disabled=false;
		document.getElementById("i_imageFileName").disabled=false;		
	}
	function fn_modify_article(obj) {
		obj.action="${contextPath}/board/modArticle.do";
		obj.submit();
	}
	function fn_remove_article(url, articleNo){
		var form=document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", url);
		var articleNoInput=document.createElement("input");
		articleNoInput.setAttribute("type", "hidden");
		articleNoInput.setAttribute("name", "articleNo");
		articleNoInput.setAttribute("value", articleNo);
		form.appendChild(articleNoInput);
		document.body.appendChild(form);
		form.submit();
	}
	function fn_reply_form(url, parentNo) {
		var form=document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", url);
		var parentNoInput=document.createElement("input");
		parentNoInput.setAttribute("type", "hidden");
		parentNoInput.setAttribute("name", "parentNo");
		parentNoInput.setAttribute("value", parentNo);
		form.appendChild(parentNoInput);
		document.body.appendChild(form);
		form.submit();
	}
</script>
</head>
<body>
<form action="${contextPath }" name="frmArticle" method="post" enctype="multipart/form-data">
<table>
	<tr>
		<td class="col1">글번호</td>
		<td><input type="text" value="${articleVO.articleNo}" name="articleNo" disabled/>
			<input type="hidden" name="articleNo" value="${articleVO.articleNo}"/>
		</td>
	</tr>
	<tr>
		<td class="col1">작성자</td>
		<td><input type="text" value="${articleVO.id}" name="writer" disabled/></td>
	</tr>
	<tr>
		<td class="col1">글제목</td>
		<td><input type="text" value="${articleVO.title}" name="title" id="i_title" disabled/></td>
	</tr>
	<tr>
		<td class="col1">글내용</td>
		<td><textarea name="content" id="i_content" cols="60" rows="20" disabled>${articleVO.content }</textarea></td>
	</tr>
	<c:if test="${not empty articleVO.imageFileName && article.imageFilName!='null' }">
		<tr>
			<td class="col1" rowspan="2">이미지</td>
			<td>
				<input type="hidden" name="originalFileName" value="${articleVO.imageFileName }"/>
				<img src="${contextPath }/download.do?imageFileName=${articleVO.imageFileName}&articleNo=${articleVO.articleNo}" id=preview />
			</td>
		</tr>
		<tr>
			<td>
				<input type="file" name="imageFileName" id="i_imageFileName" disabled onchange="readURL(this)"/>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="col1">등록일자</td>
		<td><input type="text" value="<fmt:formatDate value='${articleVO.writeDate}'/>" name="writeDate" id="i_writeDate" disabled/></td>
	</tr>
	<tr id="tr_btn_modify" style="display: none">
		<td colspan="2" >
			<input type="button" value="수정반영하기" onclick="fn_modify_article(frmArticle)"/>
			<input type="button" value="취소" onclick="backToList(frmArticle)"/>
		</td>
	</tr>
	<tr id="tr_btn">
		<td class="col2" colspan="2">
			<input type="button" value="수정하기" onclick="fn_enable(this.form)"/>
			<input type="button" value="삭제하기" 
			onclick="fn_remove_article('${contextPath}/board/removeArticle.do', ${articleVO.articleNo })"/>
			<input type="button" value="목록보기" onclick="backToList(this.form)" />
			<input type="button" value="답글쓰기" 
			onclick="fn_reply_form('${contextPath}/board/replyForm.do', ${articleVO.articleNo })"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>