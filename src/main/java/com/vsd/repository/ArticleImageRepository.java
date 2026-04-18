package com.vsd.repository;

import com.vsd.entity.Article;
import com.vsd.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleImageRepository extends JpaRepository<ArticleImage,Long> {

    List<ArticleImage> findByArticle(Article article);
}
