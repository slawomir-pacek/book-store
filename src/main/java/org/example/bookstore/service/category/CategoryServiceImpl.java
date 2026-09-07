package org.example.bookstore.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryId;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.category.CategoryRepository;
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
    public List<CategoryDto> findAll() {
        return categoryRepository.findAllActive()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findActiveById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Category with id " + id + " not found"));

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findActiveById(id)
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
        Category category = categoryRepository.findActiveById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Category with id " + id + " not found"));

        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDtoWithoutCategoryId> getBooksByCategoryId(
            Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
