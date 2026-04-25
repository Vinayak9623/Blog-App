package com.vsd.controller;

import com.vsd.dto.ArticleDto;
import com.vsd.dto.ArticleImageDto;
import com.vsd.service.ArticleService;
import com.vsd.service.impl.ArticleServiceImpl;
import io.minio.errors.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
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

    @GetMapping("/articlePage")
    public Page<ArticleDto> getPagenatedArticles(
            @RequestParam(name = "page",defaultValue = "1") Integer page ,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ){
        return articleService.getPeginatedArticle(page, size);
    }

    @GetMapping("/single/{id}")
    public ArticleDto getSingleArticle(@PathVariable("id") Long articleId) {
        return articleService.getSingleArticle(articleId);
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle( @PathVariable Long articleId) {
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

    @PostMapping("/images/{articleId}")
    public List<ArticleImageDto> uploadImages
            (@RequestParam("articleImages") List<MultipartFile> files,
                                              @PathVariable Long articleId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return articleService.uploadImage(files,articleId);

    }
}
