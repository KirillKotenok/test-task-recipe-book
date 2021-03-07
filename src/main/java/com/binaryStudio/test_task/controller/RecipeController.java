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
  public ResponseEntity<RecipeDto> save_recipeDto( @Validated @RequestBody RecipeDto recipeDto) {
    RecipeDto saved_recipe = service.save_recipe(recipeDto);
    return new ResponseEntity<>(saved_recipe, HttpStatus.CREATED);
  }

  @PostMapping("/{id}")
  public ResponseEntity<RecipeDto> save_child_recipeDto( @Validated @RequestBody RecipeDto childRecipeDto,
                                                         @PathVariable("id") Long parentId) {
    childRecipeDto.setParentRecipe(service.find_by_id(parentId));
    RecipeDto saved_recipe = service.save_recipe(childRecipeDto);
    return new ResponseEntity<>(saved_recipe, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<RecipeDto> update_recipeDto( @Validated @RequestBody RecipeDto recipeDto) {
    RecipeDto updated_recipe = service.update_recipe(recipeDto);
    return new ResponseEntity<>(updated_recipe, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> delete_recipeDto(@PathVariable Long id) {
    service.delete_by_id(id);
    return new ResponseEntity<String>("Deleted successful", HttpStatus.OK);
  }

  @GetMapping("/all")
  public ResponseEntity<List<RecipeDto>> find_all_recipeDtos() {
    List<RecipeDto> recipeDtoList = service.find_all();
    return new ResponseEntity<>(recipeDtoList, HttpStatus.OK);
  }

  @GetMapping("/child/{id}")
  public ResponseEntity<List<RecipeDto>> find_all_child_recipeDtos(@PathVariable("id") RecipeDto parentRecipeDto) {
    List<RecipeDto> childRecipeDtoList = service.find_all_by_parent_recipe(parentRecipeDto);
    return new ResponseEntity<>(childRecipeDtoList, HttpStatus.OK);
  }

  @GetMapping("recipe_without_parent")
  public ResponseEntity<List<RecipeDto>> find_all_recipeDtos_without_parent() {
    List<RecipeDto> recipeWithoutParentList = service.find_all_recipe_without_parent();
    return new ResponseEntity<>(recipeWithoutParentList, HttpStatus.OK);
  }
}
