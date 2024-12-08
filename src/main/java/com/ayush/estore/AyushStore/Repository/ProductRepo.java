package com.ayush.estore.AyushStore.Repository;

import java.util.Locale.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ayush.estore.AyushStore.entities.Catergory;
import com.ayush.estore.AyushStore.entities.Product;

public interface ProductRepo extends JpaRepository<Product, String> {
    //
    Page<Product> findByTitleContaining(String subTitle, Pageable pageable);

    // @Query(value = "SELECT p FROM Product p WHERE p.is_live = true")
    Page<Product> findByisLiveTrue(Pageable pageable);

    Page<Product> findByCatergory(Catergory category, Pageable pageable);
}