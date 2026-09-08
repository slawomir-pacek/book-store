package org.example.bookstore.service.category;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryRequestDto;
import org.example.bookstore.dto.category.UpdateCategoryRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.category.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Category with id " + id + " not found"));

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto update(
            Long id,
            UpdateCategoryRequestDto categoryDto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Category with id " + id + " not found"));

        category.setName(categoryDto.name());
        category.setDescription(categoryDto.description());

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Category with id " + id + " not found"));

        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            Long categoryId, Pageable pageable) {

        categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Category with id " + categoryId + " not found"));

        return bookRepository.findAllByCategoriesId(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
