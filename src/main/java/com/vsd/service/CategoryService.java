package com.vsd.service;

import com.vsd.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

 public CategoryDto create(CategoryDto categoryDto);
 public List<CategoryDto> getCategories();
 public CategoryDto getCategoryById(Long categoryId);
 public String deleteCategoryById(Long categoryId);
}
