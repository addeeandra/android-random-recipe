package me.inibukanadit.randomrecipe.app.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe ORDER BY lastUpdated DESC LIMIT 1")
    fun getLatestRecipe(): LiveData<Recipe?>

    @Query("SELECT * FROM recipe WHERE id = :id LIMIT 1")
    fun getRecipeById(id: Int): Recipe?

    @Insert
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)

}