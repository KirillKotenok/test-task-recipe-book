package com.binaryStudio.test_task.service.impl;

import com.binaryStudio.test_task.dto.RecipeDto;
import com.binaryStudio.test_task.entity.Recipe;
import com.binaryStudio.test_task.exception.ResourceNotFoundException;
import com.binaryStudio.test_task.mapper.RecipeMapper;
import com.binaryStudio.test_task.repo.RecipeRepo;
import com.binaryStudio.test_task.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

  private transient final RecipeMapper recipeMapper;
  private transient final RecipeRepo recipeRepo;

  @Override
  @Transactional
  public RecipeDto  save_recipe(RecipeDto recipeDto) {
    Recipe recipe = recipeMapper.toEntity(recipeDto);
    Recipe savedRecipe = recipeRepo.save(recipe);
    RecipeDto savedRecipeDto = recipeMapper.toDto(savedRecipe);
    return savedRecipeDto;
  }

  @Override
  public RecipeDto update_recipe(RecipeDto recipeDto) {
    return save_recipe(recipeDto);
  }

  @Override
  public List<RecipeDto> find_all() {
    return recipeMapper.toDtoList(recipeRepo.findAll(Sort.by("name")));
  }

  @Override
  public RecipeDto find_by_id(Long recipeId) {
    Optional<Recipe> recipeFromDb = recipeRepo.findByRecipeId(recipeId);
    RecipeDto recipeDtoFromDb = recipeFromDb.map(recipe -> recipeMapper.toDto(recipe))
            .orElseThrow(() -> new ResourceNotFoundException("Recipe with this id does not exist!"));
    return recipeDtoFromDb;
  }

  @Override
  public List<RecipeDto> find_all_by_parent_recipe(RecipeDto parentDto) {
    Recipe parent = recipeMapper.toEntity(parentDto);
    List<Recipe> childRecipes = recipeRepo.findAllByParentRecipe(parent);
    List<RecipeDto> childRecipesDto = recipeMapper.toDtoList(childRecipes);
    return childRecipesDto;
  }

  @Override
  public void delete_by_id(Long recipeId) {
    Optional<Recipe> recipeFromDB = recipeRepo.findById(recipeId);
    if (recipeFromDB.isPresent()) {
      recipeRepo.deleteById(recipeId);
    } else {
      throw new ResourceNotFoundException();
    }
  }

  @Override
  public void delete_all() {
    recipeRepo.deleteAll();
  }

  @Override
  public List<RecipeDto> find_all_recipe_without_parent() {
    List<RecipeDto> recipeWithoutParentList = recipeMapper.toDtoList(recipeRepo.findAllByParentRecipeIsNull());
    return recipeWithoutParentList;
  }
}