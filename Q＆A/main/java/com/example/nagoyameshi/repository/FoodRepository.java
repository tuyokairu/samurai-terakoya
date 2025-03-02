package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Food;

public interface FoodRepository extends JpaRepository<Food, Integer>{
	public Page<Food> findByNameLike(String keyword, Pageable pageable);
	
	
	 public List<Food> findTop10ByOrderByCreatedAtDesc();

}
