package com.vsd.controller;

import com.vsd.service.CategoryService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService=categoryService;
    }

    public void create(){
        categoryService.create();
    }
}
