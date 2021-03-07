package com.binaryStudio.test_task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeDto {

  private Long recipeId;

  @NotNull
  private String name;

  @NotNull
  private String description;

  private Date created;

  private RecipeDto parentRecipe;

  public RecipeDto(@NotNull String name, @NotNull String description) {
    this.name = name;
    this.description = description;
  }

  public RecipeDto(Long recipeId, @NotNull String name, @NotNull String description, Date created) {
    this.recipeId = recipeId;
    this.name = name;
    this.description = description;
    this.created = created;
  }

  public RecipeDto(@NotNull String name, @NotNull String description, RecipeDto parentRecipe) {
    this.name = name;
    this.description = description;
    this.parentRecipe = parentRecipe;
  }
}

