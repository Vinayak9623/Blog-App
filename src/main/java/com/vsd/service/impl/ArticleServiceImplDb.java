package com.vsd.service.impl;

import com.vsd.dto.ArticleDto;
import com.vsd.entity.Article;
import com.vsd.exception.ResourceNotFoundException;
import com.vsd.repository.ArticleRepository;
import com.vsd.repository.CategoryRepository;
import com.vsd.repository.UserRepository;
import com.vsd.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class ArticleServiceImplDb implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public ArticleDto create(ArticleDto articleDto) {
        var article=modelMapper.map(articleDto, Article.class);
        article.setCreatedAt(LocalDateTime.now());
        var save=articleRepository.save(article);
        return modelMapper.map(save,ArticleDto.class);
    }



    @Override
    public ArticleDto update(Long articalId, ArticleDto articleDto) {

        var article=articleRepository.findById(articalId)
                .orElseThrow(()->new RuntimeException("Article not found"));

        article.setTitle(articleDto.getTitle());
        article.setShortDesc(articleDto.getShortDesc());
        article.setPrice(articleDto.getPrice());
        article.setContent(articleDto.getContent());
        article.setRating(articleDto.getRating());
        article.setStatus(articleDto.getStatus());
        var updateArticle=articleRepository.save(article);
        return modelMapper.map(updateArticle,ArticleDto.class);
    }

    @Override
    public List<ArticleDto> getArticles() {
        return articleRepository.findAll()
                .stream()
                .map(article->modelMapper
                        .map(article,ArticleDto
                                .class)).toList();

    }

    @Override
    public ArticleDto getSingleArticle(long articleId) {
        var article=articleRepository.findById(articleId)
                .orElseThrow(()->new ResourceNotFoundException("Article not found"));
        return modelMapper.map(article,ArticleDto.class);
    }

    @Override
    public String deleteArticle(long articleId) {
        var article=articleRepository.findById(articleId)
                .orElseThrow(()->new RuntimeException("Article not found"));
        articleRepository.delete(article);
        return "Deleted Successfully with"+articleId;
    }

    @Override
    public List<ArticleDto> getArticleOfUser(Long userId) {
     var user=userRepository.findById(userId)
             .orElseThrow(()->new
                     ResourceNotFoundException("user not found"));
     return articleRepository
             .findByUser(user)
             .stream()
             .map(x->modelMapper.map(x, ArticleDto.class)).toList();

    }

    @Override
    public List<ArticleDto> getArticleOfCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return articleRepository.findByCategory(category).stream().map(x->modelMapper
                .map(x, ArticleDto.class)).toList();

    }
}
