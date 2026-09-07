package org.example.bookstore.repository.category;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false")
    List<Category> findAllActive();

    @Query("SELECT c FROM Category c WHERE c.id = :id AND c.isDeleted = false")
    Optional<Category> findActiveById(@Param("id") Long id);
}
