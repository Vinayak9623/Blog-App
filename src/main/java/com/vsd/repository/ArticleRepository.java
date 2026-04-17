package com.vsd.repository;

import com.vsd.entity.Article;
import com.vsd.entity.Category;
import com.vsd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {

    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
}
