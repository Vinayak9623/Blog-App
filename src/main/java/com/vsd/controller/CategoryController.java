package com.vsd.controller;

import com.vsd.dto.CategoryDto;
import com.vsd.service.CategoryService;
import com.vsd.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/")
    public CategoryDto create(@RequestBody CategoryDto categoryDto){
        return categoryService.create(categoryDto);
    }

    @GetMapping("/")
    public List<CategoryDto> getCategory(){
        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryFyId(@PathVariable Long categoryId){
        return categoryService.getCategoryById(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        return categoryService.deleteCategoryById(categoryId);
    }

}
