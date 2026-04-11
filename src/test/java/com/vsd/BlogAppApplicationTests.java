package com.vsd;

import com.vsd.controller.CategoryController;
import com.vsd.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogAppApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryController categoryController;

	@Test
	void contextLoads() {
	}

    @Test
    public void createCategory(){
        System.out.println("Category service");
        categoryService.create();
    }

    @Test
    public void createCategoryController(){
        System.out.println("Category controller");
        categoryController.create();
    }
}
