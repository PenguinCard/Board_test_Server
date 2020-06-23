package com.java.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardService {
	
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO=new BoardDAO();
	}
	
	public int addArticle(ArticleVO articleVO) {
		return boardDAO.insertNewArticle(articleVO);
	}
	
	public List<ArticleVO> listArticle(){
		List<ArticleVO> articleList=boardDAO.selectAllArticles();
		return articleList;
	}
	
	public ArticleVO viewArticle(int articleNo) {
		ArticleVO articleVO=null;
		articleVO=boardDAO.selectArticle(articleNo);
		return articleVO;
	}
	
	public List<Integer> removeArticle(int articleNo) {
		List<Integer> articleNoList=boardDAO.selectRemovedArticles(articleNo);
		boardDAO.deleteArticle(articleNo);
		return articleNoList;
	}
	
	public List<Member> countMember() {
		return boardDAO.countMember();
	} 
	public void modArticle(ArticleVO articleVO) {
		boardDAO.updateArticle(articleVO);
	}
	public int addReply(ArticleVO articleVO) {
		return boardDAO.insertNewArticle(articleVO);
	}
	public Map<String, Object> listArticles(Map<String, Integer> pagingMap) {
		Map<String, Object> articleMap=new HashMap<>();
		List<ArticleVO> articleList=boardDAO.selectAllArticles(pagingMap);
		int toArticles=boardDAO.selectToArticles();
		articleMap.put("articleList", articleList);
		articleMap.put("toArticles", toArticles);
		return articleMap;
	}
}
