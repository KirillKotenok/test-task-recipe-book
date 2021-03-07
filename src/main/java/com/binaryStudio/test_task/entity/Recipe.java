package com.binaryStudio.test_task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "recipe")
public class Recipe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false)
  private Long recipeId;

  @NotNull
  @Column(name = "name")
  private String name;

  @NotNull
  @Column(name = "description")
  private String description;

  @Column(name = "created", updatable = false)
  @CreationTimestamp
  @Temporal(value = TemporalType.DATE)
  private Date created;

  @ManyToOne(fetch = FetchType.LAZY)
  private Recipe parentRecipe;
}
