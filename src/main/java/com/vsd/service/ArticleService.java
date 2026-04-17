package com.vsd.service;

import com.vsd.dto.ArticleDto;

import java.util.List;

public interface ArticleService {
    public ArticleDto create(ArticleDto articleDto);
    public ArticleDto update(Long articalId,ArticleDto articleDto);
    public List<ArticleDto> getArticles();
    public ArticleDto getSingleArticle(long articleId);
    public String deleteArticle(long articleId);
    public List<ArticleDto> getArticleOfUser(Long userId);
    public List<ArticleDto> getArticleOfCategory(Long category);
}
