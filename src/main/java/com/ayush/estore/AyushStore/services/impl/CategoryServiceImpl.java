package com.ayush.estore.AyushStore.services.impl;

import java.util.UUID;
import java.util.Locale.Category;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ayush.estore.AyushStore.Repository.CategoryRepo;
import com.ayush.estore.AyushStore.dtos.CategoryDto;
import com.ayush.estore.AyushStore.dtos.PageableResponse;
import com.ayush.estore.AyushStore.entities.Catergory;
import com.ayush.estore.AyushStore.exceptions.ResourceNotFoundException;
import com.ayush.estore.AyushStore.helper.Helper;
import com.ayush.estore.AyushStore.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        // TODO Auto-generated method stub
        String categoryId = UUID.randomUUID().toString();
        // categoryDto.setCategoryId(categoryId);
        categoryDto.setCategoryId(categoryId);
        Catergory category = mapper.map(categoryDto, Catergory.class);

        Catergory savedCategory = categoryRepo.save(category);
        return mapper.map(savedCategory, CategoryDto.class);

    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        // TODO Auto-generated method stub
        Catergory category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        // update category details

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Catergory updatedCategory = categoryRepo.save(category);
        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        // TODO Auto-generated method stub
        Catergory category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        categoryRepo.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getAll'");

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Catergory> page = categoryRepo.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        // TODO Auto-generated method stub
        Catergory category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with given id !!"));
        return mapper.map(category, CategoryDto.class);
    }

}
