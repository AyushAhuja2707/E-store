package com.ayush.estore.AyushStore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.estore.AyushStore.entities.Catergory;

public interface CategoryRepo extends JpaRepository<Catergory, String> {

}
