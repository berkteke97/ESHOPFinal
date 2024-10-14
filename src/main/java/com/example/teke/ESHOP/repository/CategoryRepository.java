package com.example.teke.ESHOP.repository;

import com.example.teke.ESHOP.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CategoryRepository extends CrudRepository<Category, UUID> {
}
