package com.binaryStudio.test_task.service;

import com.binaryStudio.test_task.dto.RecipeDto;

import java.util.List;

public interface RecipeService {

  RecipeDto save_recipe(RecipeDto recipeDto);

  RecipeDto update_recipe(RecipeDto recipeDto);

  List<RecipeDto> find_all();

  RecipeDto find_by_id(Long recipeId);

  List<RecipeDto> find_all_by_parent_recipe(RecipeDto parentDto);

  void delete_by_id(Long recipeId);

  void delete_all();

  List<RecipeDto> find_all_recipe_without_parent();
}
