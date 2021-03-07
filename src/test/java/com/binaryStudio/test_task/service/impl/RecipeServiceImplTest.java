package com.binaryStudio.test_task.service.impl;

import com.binaryStudio.test_task.TestTaskApplication;
import com.binaryStudio.test_task.dto.RecipeDto;
import com.binaryStudio.test_task.exception.ResourceNotFoundException;
import com.binaryStudio.test_task.mapper.RecipeMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestTaskApplication.class})
public class RecipeServiceImplTest {

  private RecipeServiceImpl recipeService;
  private RecipeMapper recipeMapper;

  @Autowired
  public RecipeServiceImplTest(RecipeServiceImpl recipeService, RecipeMapper recipeMapper) {
    this.recipeService = recipeService;
    this.recipeMapper = recipeMapper;
  }

  private RecipeDto testRecipeDto1;
  private RecipeDto testRecipeDto2;
  private RecipeDto testRecipeDto3;

  @BeforeEach
  public void init() {
    testRecipeDto1 = new RecipeDto("Fried fish", "Take the fish and roast it.");
    testRecipeDto2 = new RecipeDto("Fried fish with Mayo", "Take the fish and roast it. Don`t forget mayo");
    testRecipeDto3 = new RecipeDto("Fried chicken", "Take the chicken and roast it.");
  }

  @AfterEach
  public void clearDB() {
    recipeService.delete_all();
  }

  @Test
  public void wheNewRecipeComeInThanSaveItInDatabase() {
    RecipeDto savedDto = recipeService.save_recipe(testRecipeDto1);
    testRecipeDto1.setRecipeId(savedDto.getRecipeId());
    testRecipeDto1.setCreated(savedDto.getCreated());
    assertEquals(testRecipeDto1, savedDto);
  }

  @Test
  public void whenNewChildRecipeComeInThanSaveItInDatabaseWithParent() {
    RecipeDto savedDto1 = recipeService.save_recipe(testRecipeDto1);
    testRecipeDto1.setRecipeId(savedDto1.getRecipeId());
    testRecipeDto1.setCreated(savedDto1.getCreated());
    testRecipeDto2.setParentRecipe(savedDto1);
    RecipeDto saveChildDto = recipeService.save_recipe(testRecipeDto2);
    assertEquals(saveChildDto.getParentRecipe().getRecipeId(), testRecipeDto1.getRecipeId());
  }

  @Test
  public void whenTryDeleteRecipeThatDoesntExistThanThrowException() {
    assertThrows(ResourceNotFoundException.class, () -> recipeService.delete_by_id(getIdThatDoesNotExist()));
  }

  @Test
  public void wheDeleteRecipeByIdThanDeleteItFromDatabase() {
    RecipeDto savedDto = recipeService.save_recipe(testRecipeDto1);
    recipeService.delete_by_id(savedDto.getRecipeId());
    assertThrows(ResourceNotFoundException.class, () -> recipeService.find_by_id(savedDto.getRecipeId()));
  }

  @Test
  public void whenUpdateRecipeThanUpdateItInDatabase() {
    RecipeDto savedDto = recipeService.save_recipe(testRecipeDto1);
    savedDto.setDescription("Simple test description");
    RecipeDto updateDto = recipeService.update_recipe(savedDto);
    assertEquals(updateDto.getDescription(), recipeService.find_by_id(savedDto.getRecipeId()).getDescription());
  }

  @Test
  public void whenFindAllRecipesThanReturnAllRecipesFromDatabaseSortedByName() {
    RecipeDto savedDto1 = recipeService.save_recipe(testRecipeDto1);
    testRecipeDto2.setParentRecipe(savedDto1);
    recipeService.save_recipe(testRecipeDto2);
    recipeService.save_recipe(testRecipeDto3);

    assertEquals(3, recipeService.find_all().size());
  }

  @Test
  public void whenSaveChildRecipesAndGetItThanReturnAllChildRecipes() {
    RecipeDto savedParentRecipe = recipeService.save_recipe(testRecipeDto1);
    testRecipeDto2.setParentRecipe(savedParentRecipe);
    recipeService.save_recipe(testRecipeDto2);
    testRecipeDto3.setParentRecipe(savedParentRecipe);
    recipeService.save_recipe(testRecipeDto3);
    List<RecipeDto> childRecipesList = recipeService.find_all_by_parent_recipe(savedParentRecipe);
    assertEquals(2, childRecipesList.size());
  }

  @Test
  public void whenDeleteAllRecipesThanDatabaseIsEmpty() {
    RecipeDto savedParentRecipe = recipeService.save_recipe(testRecipeDto1);
    testRecipeDto2.setParentRecipe(savedParentRecipe);
    recipeService.save_recipe(testRecipeDto2);
    testRecipeDto3.setParentRecipe(savedParentRecipe);
    recipeService.save_recipe(testRecipeDto3);
    recipeService.delete_all();
    assertTrue(recipeService.find_all().isEmpty());
  }

  @Test
  public void whenFindAllRecipeWithoutParentThanReturnRecipes() {
    recipeService.save_recipe(testRecipeDto1);
    recipeService.save_recipe(testRecipeDto2);
    recipeService.save_recipe(testRecipeDto3);

    assertEquals(3, recipeService.find_all_recipe_without_parent().size());
  }

  private Long getIdThatDoesNotExist() {
    Long max = 0L;
    for (RecipeDto r : recipeService.find_all()) {
      if (max < r.getRecipeId())
        max = r.getRecipeId();
    }
    return max + 1;
  }
}

