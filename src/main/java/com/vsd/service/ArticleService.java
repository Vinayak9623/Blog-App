package com.vsd.service;

import com.vsd.dto.ArticleDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ArticleService {

    List<ArticleDto> list=new ArrayList();
    Random random=new Random();
    public ArticleService(){
        ArticleDto articleDto=ArticleDto.builder()
                .id(1l)
                .name("Java Developer")
                .content("This is my new developer journey")
                .timeInMinutes(5)
                .build();
        list.add(articleDto);
    }

    public ArticleDto create(ArticleDto articleDto){
        articleDto.setId(random.nextLong(23456));
        list.add(articleDto);
        return articleDto;
    }

    public String update(Long articalId,ArticleDto articleDto){
        return list.stream().filter(x -> x.getId() == articalId).findFirst()
                .map(x->{
                    x.setName(articleDto.getName());
                x.setContent(articleDto.getContent());
                return "Updated Successfully!";})
                .orElse("Article not found");
    }

    public List<ArticleDto> getArticles(){
        return list;
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

}
