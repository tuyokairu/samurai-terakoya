package com.example.nagoyameshi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Food;
import com.example.nagoyameshi.form.FoodEditForm;
import com.example.nagoyameshi.form.FoodRegisterForm;
import com.example.nagoyameshi.repository.FoodRepository;

@Service
public class FoodService {
   private final FoodRepository foodRepository;    
   
   public FoodService(FoodRepository foodRepository) {
       this.foodRepository = foodRepository;        
   }    
   
   @Transactional
   public void create(FoodRegisterForm foodRegisterForm) {
	   Food food = new Food();        
       MultipartFile imageFile = foodRegisterForm.getImageFile();
       
       if (!imageFile.isEmpty()) {
           String imageName = imageFile.getOriginalFilename(); 
           String hashedImageName = generateNewFileName(imageName);
           Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
           copyImageFile(imageFile, filePath);
           food.setImageName(hashedImageName);
       }
       
       food.setName(foodRegisterForm.getName());                
       food.setDescription(foodRegisterForm.getDescription());
       food.setPrice(foodRegisterForm.getPrice());
       food.setPostalCode(foodRegisterForm.getPostalCode());
       food.setAddress(foodRegisterForm.getAddress());
       food.setPhoneNumber(foodRegisterForm.getPhoneNumber());
                   
       foodRepository.save(food);
   }  
   
   @Transactional
   public void update(FoodEditForm foodEditForm) {
	   Food food = foodRepository.getReferenceById(foodEditForm.getId());
       MultipartFile imageFile = foodEditForm.getImageFile();
       
       if (!imageFile.isEmpty()) {
           String imageName = imageFile.getOriginalFilename(); 
           String hashedImageName = generateNewFileName(imageName);
           Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
           copyImageFile(imageFile, filePath);
           food.setImageName(hashedImageName);
       }
       
       food.setName(foodEditForm.getName());                
       food.setDescription(foodEditForm.getDescription());
       food.setPrice(foodEditForm.getPrice());
       food.setPostalCode(foodEditForm.getPostalCode());
       food.setAddress(foodEditForm.getAddress());
       food.setPhoneNumber(foodEditForm.getPhoneNumber());
                   
       foodRepository.save(food);
   }    
   
   // UUIDを使って生成したファイル名を返す
   public String generateNewFileName(String fileName) {
       String[] fileNames = fileName.split("\\.");                
       for (int i = 0; i < fileNames.length - 1; i++) {
           fileNames[i] = UUID.randomUUID().toString();            
       }
       String hashedFileName = String.join(".", fileNames);
       return hashedFileName;
   }     
   
   // 画像ファイルを指定したファイルにコピーする
   public void copyImageFile(MultipartFile imageFile, Path filePath) {           
       try {
           Files.copy(imageFile.getInputStream(), filePath);
       } catch (IOException e) {
           e.printStackTrace();
       }          
   } 
}