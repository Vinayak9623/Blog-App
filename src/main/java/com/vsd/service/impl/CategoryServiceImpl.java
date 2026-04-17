package com.vsd.service.impl;

import com.vsd.dto.CategoryDto;
import com.vsd.entity.Category;
import com.vsd.exception.ResourceNotFoundException;
import com.vsd.repository.CategoryRepository;
import com.vsd.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        var category=modelMapper.map(categoryDto, Category.class);
        categoryRepository.save(category);
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(x->modelMapper
                .map(x,CategoryDto.class)).toList();
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        var category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found"));
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public String deleteCategoryById(Long categoryId) {
        var category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
        return "Deleted successfully";
    }
}
