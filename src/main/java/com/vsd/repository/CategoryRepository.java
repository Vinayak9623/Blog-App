package com.vsd.repository;

import com.vsd.dto.ArticleDto;
import com.vsd.entity.Article;
import com.vsd.entity.Category;
import com.vsd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {


}
