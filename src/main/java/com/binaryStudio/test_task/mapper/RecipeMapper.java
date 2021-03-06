package com.binaryStudio.test_task.mapper;

import com.binaryStudio.test_task.dto.RecipeDto;
import com.binaryStudio.test_task.entity.Recipe;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

  Recipe toEntity(RecipeDto recipeDto);

  RecipeDto toDto(Recipe recipe);

  List<RecipeDto> toDtoList(List<Recipe> recipes);

  List<Recipe> toEntityList(List<RecipeDto> recipesDto);
}
