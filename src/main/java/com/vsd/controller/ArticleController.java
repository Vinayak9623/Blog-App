package com.vsd.controller;

import com.vsd.dto.ArticleDto;
import com.vsd.service.ArticleService;
import com.vsd.service.impl.ArticleServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    @PostMapping("/")
    public ArticleDto createArticle(@RequestBody ArticleDto articleDto) {
    return articleService.create(articleDto);
    }

    @PutMapping("/{articleId}")
    public ArticleDto updateArticle(@PathVariable long articleId,@RequestBody ArticleDto articleDto){
        return articleService.update(articleId,articleDto);
    }
    @GetMapping("/get")
    public List<ArticleDto> getArticles() {
        return articleService.getArticles();
    }

    @GetMapping("/{articleId}")
    public ArticleDto getSingleArticle(@PathVariable long articleId) {
        return articleService.getSingleArticle(articleId);
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle( @PathVariable long articleId) {
        return articleService.deleteArticle(articleId);
    }

    @GetMapping("/{userId}")
    public List<ArticleDto> getArticleByUser(@PathVariable Long userId){
        return articleService.getArticleOfUser(userId);
    }

    @GetMapping("/{categoryId}")
    public List<ArticleDto> getArticeByCategory(@PathVariable Long categoryId){
        return articleService.getArticleOfCategory(categoryId);
    }
}
