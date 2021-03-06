package com.binaryStudio.test_task.controller;

import com.binaryStudio.test_task.dto.RecipeDto;
import com.binaryStudio.test_task.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

  private transient final RecipeService service;

  @PostMapping
  public ResponseEntity<RecipeDto> save_RecipeDto(@RequestBody RecipeDto recipeDto) {
    RecipeDto saved_recipe = service.save_recipe(recipeDto);
    return new ResponseEntity<>(saved_recipe, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<RecipeDto> update_RecipeDto(@RequestBody @Validated RecipeDto recipeDto) {
    RecipeDto updated_recipe = service.update_recipe(recipeDto);
    return new ResponseEntity<>(updated_recipe, HttpStatus.OK);
  }

  @DeleteMapping("/{recipeId}")
  public ResponseEntity<String> delete_RecipeDto(@PathVariable Long recipeId) {
    service.delete_by_id(recipeId);
    return new ResponseEntity<String>("Deleted successful", HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<RecipeDto>> find_all_RecipeDtos() {
    List<RecipeDto> recipeDtoList = service.find_all();
    return new ResponseEntity<>(recipeDtoList, HttpStatus.OK);
  }

  @GetMapping("/child")
  public ResponseEntity<List<RecipeDto>> find_all_child_RecipeDtos(@RequestBody @Validated RecipeDto parentRecipeDto) {
    List<RecipeDto> childRecipeDtoList = service.find_all_by_parent_recipe(parentRecipeDto);
    return new ResponseEntity<>(childRecipeDtoList, HttpStatus.OK);
  }

  @GetMapping("/recipe_without_parent")
  public ResponseEntity<List<RecipeDto>> find_all_RecipeDtos_without_parent() {
    List<RecipeDto> recipeWithoutParentList = service.find_all_recipe_without_parent();
    return new ResponseEntity<>(recipeWithoutParentList, HttpStatus.OK);
  }
}
