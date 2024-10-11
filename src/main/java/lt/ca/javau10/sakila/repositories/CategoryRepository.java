package lt.ca.javau10.sakila.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Short> {
    boolean existsByName(String name);
}