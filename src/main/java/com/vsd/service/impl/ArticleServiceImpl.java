package com.vsd.service.impl;

import com.vsd.dto.ArticleDto;
import com.vsd.dto.ArticleImageDto;
import com.vsd.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ArticleServiceImpl implements ArticleService {

    List<ArticleDto> list=new ArrayList();
    Random random=new Random();
    public ArticleServiceImpl(){
        ArticleDto articleDto=ArticleDto.builder()
                .id(1l)
                .content("This is my new developer journey")
                .build();
        list.add(articleDto);
    }

    public ArticleDto create(ArticleDto articleDto){
        articleDto.setId(random.nextLong(23456));
        list.add(articleDto);
        return articleDto;
    }

    public ArticleDto update(Long articalId,ArticleDto articleDto){

        ArticleDto first = list.stream().findFirst().orElseThrow();
        return first;

    }

    public List<ArticleDto> getArticles(){
        return list;
    }

    @Override
    public Page<ArticleDto> getPeginatedArticle(int page, int size) {
        return null;
    }

    public ArticleDto getSingleArticle(long articleId){
        return list.stream().filter(article->article
                .getId()==articleId).findFirst()
                .orElseThrow(()->new RuntimeException("Article not found"));
    }

    public String deleteArticle(long articleId){
        boolean b = list.removeIf(x -> x.getId() == articleId);
        return (b)?"Article Deleted successfully":"Article not found with given id";
    }

    @Override
    public List<ArticleDto> getArticleOfUser(Long userId) {
        return List.of();
    }

    @Override
    public List<ArticleDto> getArticleOfCategory(Long category) {
        return List.of();
    }

    @Override
    public List<ArticleImageDto> uploadImage(List<MultipartFile> files, Long articleId) {
        return List.of();
    }

}
