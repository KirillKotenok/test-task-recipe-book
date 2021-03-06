package com.binaryStudio.test_task.repo;

import com.binaryStudio.test_task.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {
  @Query("select distinct r from Recipe r join fetch r.parentRecipe pr" +
          " where r.parentRecipe = :parentRecipe order by r.name")
  List<Recipe> findAllByParentRecipe(@Param("parentRecipe") Recipe parentRecipe);

  List<Recipe> findAllByParentRecipeIsNull();

  Optional<Recipe> findByRecipeId (Long recipeId);
}
