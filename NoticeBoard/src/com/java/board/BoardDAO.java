package com.java.board;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource ds;
	Connection conn;
	PreparedStatement pstmt;
	public BoardDAO() {
		try {
			Context ctx=new InitialContext();
			Context env=(Context)ctx.lookup("java:/comp/env");
			ds=(DataSource)env.lookup("jdbc/oracle");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private int getNewArticleNo() {
		try {
			conn=ds.getConnection();
			String query="SELECT max(articleNO) from t_board";
			pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) 
				return (rs.getInt(1)+1);
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return 0;
	}
	
	public void deleteArticle(int articleNo) {
		try {
			conn=ds.getConnection();
			String query="DELETE FROM T_BOARD WHERE articleNo in ("
					+ "SELECT articleNo from t_board "
					+ "start with articleNo=? "
					+ "connect by prior articleNo=parentNo)";
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, articleNo);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public List<Integer> selectRemovedArticles(int articleNo) {
		List<Integer> articleNoList=new ArrayList<>();
		try {
			conn=ds.getConnection();
			String query="SELECT articleNo from t_board"
					+ " start with articleNo=?"
					+ " connect by prior articleNo=parentNo";
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,  articleNo);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				articleNo=rs.getInt("articleNo");
				articleNoList.add(articleNo);
			}
			pstmt.close();
			conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return articleNoList;
	}
	
	public void updateArticle(ArticleVO articleVO) {
		int articleNo=articleVO.getArticleNo();
		String title=articleVO.getTitle();
		String content=articleVO.getContent();
		String imageFileName=articleVO.getImageFileName();
		try {
			conn=ds.getConnection();
			String query="update t_board set title=?, content=?";
			if(imageFileName!=null&&imageFileName.length()!=0) 
				query+=", imageFileName=?";
			query+=" where articleNo=?";
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1,  title);
			pstmt.setString(2, content);
			if(imageFileName!=null&&imageFileName.length()!=0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNo);
			} else {
				pstmt.setInt(3, articleNo);
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public int insertNewArticle(ArticleVO articleVO) {
		int articleNo=getNewArticleNo();
		try {
			conn=ds.getConnection();
			int parentNo=articleVO.getParentNo();
			String title=articleVO.getTitle();
			String content=articleVO.getContent();
			String id=articleVO.getId();
			String imageFileName=articleVO.getImageFileName();
			String query="INSERT INTO T_BOARD(articleNo, parentNo, title, content, imageFileName, id)"
					+ " values(?, ?, ?, ?, ?, ?)";
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, articleNo);
			pstmt.setInt(2, parentNo);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch(Exception e) { e.printStackTrace(); }
		return articleNo;
	}
	
	public ArticleVO selectArticle(int articleNo) {
		ArticleVO articleVO=new ArticleVO();
		try {
			conn=ds.getConnection();
			String query="select articleNo, parentNo, title, content, imageFileName, id, writeDate"
					+ " from t_board where articleNo=?";
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1, articleNo);
			ResultSet rs=pstmt.executeQuery();
			rs.next();
			int _articleNo=rs.getInt(1);
			int parentNo=rs.getInt(2);
			String title=rs.getString(3);
			String content=rs.getString(4);
			String imageFileName=rs.getString(5);
			String id=rs.getString(6);
			Date writeDate=rs.getDate(7);
			
			articleVO.setArticleNo(_articleNo);
			articleVO.setParentNo(parentNo);
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);
			articleVO.setId(id);
			articleVO.setWriteDate(writeDate);
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return articleVO;
	}
	
	public List<ArticleVO> selectAllArticles() {
		List<ArticleVO> articleList=new ArrayList<>();
		try {
			conn=ds.getConnection();
			String query="SELECT LEVEL, articleNo, parentNo, "
					+ "title, content, id, writeDate "
					+ "from t_board "
					+ "START WITH parentNo=0 "
					+ "connect by prior articleNo=parentNo "
					+ "order siblings by articleNo desc";
			
			pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				int level=rs.getInt(1);
				int articleNo=rs.getInt(2);
				int parentNo=rs.getInt(3);
				String title=rs.getString(4);
				String content=rs.getString(5);
				String id=rs.getString(6);
				Date writeDate=rs.getDate(7);
				ArticleVO articleVo=new ArticleVO();
				articleVo.setLevel(level);
				articleVo.setArticleNo(articleNo);
				articleVo.setParentNo(parentNo);
				articleVo.setTitle(title);
				articleVo.setContent(content);
				articleVo.setId(id);
				articleVo.setWriteDate(writeDate);
				articleList.add(articleVo);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return articleList;
	}
	public List<ArticleVO> selectAllArticles(Map<String, Integer> pagingMap) {
		List<ArticleVO> articleList=new ArrayList<>();
		int section=(Integer)pagingMap.get("section");
		int pageNum=(Integer)pagingMap.get("pageNum");
		try {
			conn=ds.getConnection();
			String query="select * from ( select rownum as recNum, LVL, articleNo, "
					+ "parentNo, title, id, writeDate from (select level as LVL, "
					+ "articleNo, parentNo, title, id, writeDate from t_board "
					+ "start with parentNo=0 connect by prior articleNo=parentNo "
					+ "order siblings by articleNo desc)) where recNum between (?-1)*100"
					+ "+(?-1)*10+1 and (?-1)*100+?*10";
			pstmt=conn.prepareStatement(query);
			for(int i=1; i<=2; i++) {
				int a=2*i-1;
				pstmt.setInt(a, section); 
				int b=2*i;				
				pstmt.setInt(b, pageNum);
			}
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				int level=rs.getInt("lvl");
				int articleNo=rs.getInt("articleNo");
				int parentNo=rs.getInt("parentNo");
				String title=rs.getString("title");
				String id=rs.getString("id");
				Date writeDate=rs.getDate("writeDate");
				ArticleVO articleVO=new ArticleVO();
				articleVO.setLevel(level);
				articleVO.setArticleNo(articleNo);
				articleVO.setParentNo(parentNo);
				articleVO.setTitle(title);
				articleVO.setId(id);
				articleVO.setWriteDate(writeDate);
				articleList.add(articleVO);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch(Exception e) { e.printStackTrace(); }
		return articleList;
	}
	
	public int selectToArticles() {
		try {
			conn=ds.getConnection();
			String query="select count(articleNo) from t_board";
			pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next())
				return (rs.getInt(1));
			rs.close();
			pstmt.close();
			conn.close();
		} catch(Exception e) { e.printStackTrace(); }
		return 0;
	}
	
	public List<Member> countMember() {
		List<Member> MemberCount=new ArrayList<>();
		try {
			conn=ds.getConnection();
			String query="select id, count(id) c_id from t_board group by id order by c_id desc;";
			pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				String id=rs.getString(1);
				int count=rs.getInt(2);
				Member member=new Member();
				member.setId(id);
				member.setCount(count);
				MemberCount.add(member);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return MemberCount;
	}
}
