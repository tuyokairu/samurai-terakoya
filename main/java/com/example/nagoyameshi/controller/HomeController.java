package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Food;
import com.example.nagoyameshi.repository.FoodRepository;

@Controller

public class HomeController {
	private final FoodRepository foodRepository;        
    
    public HomeController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;            
    }    
	@GetMapping("/")
		public String index(Model model) {
	         List<Food> newFood = foodRepository.findTop10ByOrderByCreatedAtDesc();
	         model.addAttribute("newFood", newFood);  
	         
        return "index";
    }   

}
