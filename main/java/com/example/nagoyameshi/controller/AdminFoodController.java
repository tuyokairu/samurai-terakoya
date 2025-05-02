package com.example.nagoyameshi.controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Food;
import com.example.nagoyameshi.form.FoodEditForm;
import com.example.nagoyameshi.form.FoodRegisterForm;
import com.example.nagoyameshi.repository.FoodRepository;
import com.example.nagoyameshi.service.FoodService;

@Controller
@RequestMapping("/admin/food")

public class AdminFoodController {
 private final FoodRepository foodRepository;
 private final FoodService foodService;
     
 public AdminFoodController(FoodRepository foodRepository, FoodService foodService) {
         this.foodRepository = foodRepository;
         this.foodService = foodService;
     }	
     
     @GetMapping
     public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
    	 Page<Food> foodPage;   
    	 
    	 if (keyword != null && !keyword.isEmpty()) {
    		 foodPage = foodRepository.findByNameLike("%" + keyword + "%", pageable);                
         } else {
        	 foodPage = foodRepository.findAll(pageable);
         }  
         
    	 model.addAttribute("foodPage", foodPage);
    	 model.addAttribute("keyword", keyword);
         
         return "admin/food/index";
     }  
     
     @GetMapping("/{id}")
     public String show(@PathVariable(name = "id") Integer id, Model model) {
         Food food = foodRepository.getReferenceById(id);
         
         model.addAttribute("food", food);
         
         return "admin/food/show";
     }  
     
     @GetMapping("/register")
     public String register(Model model) {
         model.addAttribute("foodRegisterForm", new FoodRegisterForm());
         return "admin/food/register";
     }    
     
     @PostMapping("/create")
     public String create(@ModelAttribute @Validated FoodRegisterForm foodRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/food/register";
         }
         
         foodService.create(foodRegisterForm);
         redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");    
         
         return "redirect:/admin/food";
     }    
     
     @GetMapping("/{id}/edit")
     public String edit(@PathVariable(name = "id") Integer id, Model model) {
         Food food = foodRepository.getReferenceById(id);
         String imageName = food.getImageName();
         FoodEditForm foodEditForm = new FoodEditForm(food.getId(), food.getName(), null, food.getDescription(), food.getPrice(), food.getPostalCode(), food.getAddress(), food.getPhoneNumber());
         
         model.addAttribute("imageName", imageName);
         model.addAttribute("foodEditForm", foodEditForm);
         
         return "admin/food/edit";
     }    
     
     @PostMapping("/{id}/update")
     public String update(@ModelAttribute @Validated FoodEditForm foodEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/food/edit";
         }
         
         foodService.update(foodEditForm);
         redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
         
         return "redirect:/admin/food";
     }    
     
     @PostMapping("/{id}/delete")
     public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
         foodRepository.deleteById(id);
                 
         redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
         
         return "redirect:/admin/food";
     }   

}
