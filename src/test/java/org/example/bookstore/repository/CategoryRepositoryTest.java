package org.example.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.bookstore.model.Category;
import org.example.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindById_returnsPersistedCategory() {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        Category saved = categoryRepository.saveAndFlush(category);

        assertThat(categoryRepository.findById(saved.getId()))
                .get()
                .satisfies(found -> {
                    assertThat(found.getName()).isEqualTo("Fantasy");
                    assertThat(found.getDescription()).isEqualTo("Fantasy books");
                    assertThat(found.isDeleted()).isFalse();
                });
    }

    @Test
    void findById_doesNotReturnSoftDeletedCategory() {
        Category category = new Category();
        category.setName("Archived");

        Category saved = categoryRepository.saveAndFlush(category);
        categoryRepository.deleteById(saved.getId());
        categoryRepository.flush();

        assertThat(categoryRepository.findById(saved.getId())).isEmpty();
    }
}
