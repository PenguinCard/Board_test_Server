package com.java.board;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class BoardControl
 */
@WebServlet("/board/*")
public class BoardControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ARTICLE_IMAGE_REPO="C:\\board\\article_image";
	
	BoardService boardService;
	ArticleVO articleVO;
	
	public void init(ServletConfig config) throws ServletException {
		boardService=new BoardService();
		articleVO=new ArticleVO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nextPage="";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		HttpSession sess;
		
		String action=request.getPathInfo();
		try {
			if(action==null) {
				String _section=request.getParameter("section");
				String _pageNum=request.getParameter("pageNum");
				int section=Integer.parseInt(((_section==null)?"1":_section));
				int pageNum=Integer.parseInt(((_pageNum==null)?"1":_pageNum));
				Map<String, Integer> pagingMap=new HashMap<>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				Map<String, Object> articleMap=boardService.listArticles(pagingMap);
				articleMap.put("section", section);
				articleMap.put("pageNum", pageNum);
				request.setAttribute("articleMap", articleMap);
				nextPage="/nb/listArticles.jsp";
			} else if(action.equals("/listArticles.do")) {
				String _section=request.getParameter("section");
				String _pageNum=request.getParameter("pageNum");
				int section=Integer.parseInt(((_section==null)?"1":_section));
				int pageNum=Integer.parseInt(((_pageNum==null)?"1":_pageNum));
				Map<String, Integer> pagingMap=new HashMap<>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				Map<String, Object> articleMap=boardService.listArticles(pagingMap);
				articleMap.put("section", section);
				articleMap.put("pageNum", pageNum);
				request.setAttribute("articleMap", articleMap);
				nextPage="/nb/listArticles.jsp";
			} else if(action.equals("/articleForm.do")) {
				nextPage="/nb/articleForm.jsp";
			} else if(action.equals("/addArticle.do")) {
				int articleNo=0;
				Map<String, String> articleMap=upload(request, response);
				String title=articleMap.get("title");
				String content=articleMap.get("content");
				String imageFileName=articleMap.get("imageFileName");
				
				articleVO.setParentNo(0);
				articleVO.setId("hong");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				articleNo=boardService.addArticle(articleVO);
				
				if(imageFileName!=null&&imageFileName.length()!=0) {
					File srcFile=new File(ARTICLE_IMAGE_REPO+"\\temp\\"+imageFileName);
					File destDir=new File(ARTICLE_IMAGE_REPO+"\\"+articleNo);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				PrintWriter out=response.getWriter();
				out.print("<script> alert('새 글이 추가되었습니다'); location.href='"
						+ request.getContextPath() + "/board/listArticles.do'; </script>");
				return;
			} else if(action.equals("/viewArticle.do")) {
				String articleNo=request.getParameter("articleNo");
				articleVO=boardService.viewArticle(Integer.parseInt(articleNo));
				request.setAttribute("articleVO", articleVO);
				nextPage="/nb/viewArticle.jsp";
			} else if(action.equals("/modArticle.do")) {
				Map<String, String> articleMap=upload(request, response);
				
				int articleNo=Integer.parseInt(articleMap.get("articleNo"));
				
				articleVO.setArticleNo(articleNo);
				String title=articleMap.get("title");
				String content=articleMap.get("content");
				String imageFileName=articleMap.get("imageFileName");
				articleVO.setParentNo(0);
				articleVO.setId("hong");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				boardService.modArticle(articleVO);
				if(imageFileName!=null&&imageFileName.length()!=0) {
					String originalFileName=articleMap.get("originalFileName");
					File srcFile=new File(ARTICLE_IMAGE_REPO+"\\temp\\"+imageFileName);
					File destDir=new File(ARTICLE_IMAGE_REPO+"\\"+articleNo);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					File oldFile=new File(ARTICLE_IMAGE_REPO+"\\"+articleNo+"\\"+originalFileName);
					oldFile.delete();
				}
				PrintWriter out=response.getWriter();
				out.print("<script> alert('글이 수정되었습니다'); location.href='"
						+ request.getContextPath() + "/board/viewArticle.do?articleNo="
						+ articleNo + "'; </script>");
				return;
			} else if(action.equals("/removeArticle.do")) {
				int articleNo=Integer.parseInt(request.getParameter("articleNo"));
				List<Integer> articleNoList=boardService.removeArticle(articleNo);
				for(int _articleNo:articleNoList) {
					File imgDir=new File(ARTICLE_IMAGE_REPO+"\\"+_articleNo);
					if(imgDir.exists()) {
						FileUtils.deleteDirectory(imgDir);
					}
				}
				PrintWriter out=response.getWriter();
				out.print("<script> alert('글이 삭제되었습니다'); location.href='"
						+ request.getContextPath() + "/board/listArticles.do'; </script>");
				return;
			} else if(action.equals("/replyForm.do")) {
				int parentNo=Integer.parseInt(request.getParameter("parentNo"));
				sess=request.getSession();
				sess.setAttribute("parentNo", parentNo);
				nextPage="/nb/replyForm.jsp";
			} else if(action.equals("/addReply.do")) {
				sess=request.getSession();
				int parentNo=(Integer)sess.getAttribute("parentNo");
				sess.removeAttribute("parentNo");
				Map<String, String>articleMap=upload(request, response);
				String title=articleMap.get("title");
				String content=articleMap.get("content");
				String imageFileName=articleMap.get("imageFileName");
				articleVO.setParentNo(parentNo);
				articleVO.setId("lee");
				articleVO.setTitle(title);
				articleVO.setContent(content);
				articleVO.setImageFileName(imageFileName);
				int articleNo=boardService.addReply(articleVO);
				if(imageFileName!=null&&imageFileName.length()!=0) {
					File srcFile=new File(ARTICLE_IMAGE_REPO+"\\temp\\"+imageFileName);
					File destDir=new File(ARTICLE_IMAGE_REPO+"\\"+articleNo);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				PrintWriter out=response.getWriter();
				out.print("<script> alert('답글이 추가되었습니다'); location.href='"
						+ request.getContextPath() + "/board/viewArticle.do?articleNo="
						+ articleNo + "'; </script>");
				return;
			} else if(action.equals("/member.do")) {
				List<Member>memberList=boardService.countMember();
				request.setAttribute("memberList", memberList);
				nextPage="/nb/countMember.jsp";
			}
			RequestDispatcher dispatch=request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
		} catch (Exception e) { e.printStackTrace(); }
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String, String> articleMap=new HashMap<>();
		String encoding="utf-8";
		File currentDirPath=new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory=new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024*1024);
		ServletFileUpload upload=new ServletFileUpload(factory);
		try {
			List<FileItem> items=upload.parseRequest(request);
			for(FileItem item : items) {
				if(item.isFormField()) {
					articleMap.put(item.getFieldName(), item.getString(encoding));
				} else {
					if (item.getSize() > 0) {
						int idx=item.getName().lastIndexOf("\\");
						if(idx==-1) {
							idx=item.getName().lastIndexOf("/");
						}
						String fileName=item.getName().substring(idx+1);
						articleMap.put(item.getFieldName(), fileName);
						File uploadFile=new File(currentDirPath+"\\temp\\"+fileName);
						item.write(uploadFile);
					}
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return articleMap;
	}
}
