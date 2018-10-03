package me.inibukanadit.randomrecipe.app

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import me.inibukanadit.randomrecipe.app.data.local.Recipe
import me.inibukanadit.randomrecipe.app.data.local.RecipeDao
import me.inibukanadit.randomrecipe.app.data.local.RecipeDb
import org.json.JSONException
import org.json.JSONObject

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val mRecipeDao: RecipeDao = RecipeDb.getInstance(application).recipeDao()

    private lateinit var mLatestRecipe: LiveData<Recipe?>

    fun getLatestRecipe(): LiveData<Recipe?> {
        if (!::mLatestRecipe.isInitialized) {
            mLatestRecipe = mRecipeDao.getLatestRecipe()
        }
        return mLatestRecipe
    }

    fun fetchRandomRecipe() {
        AndroidNetworking
                .get("https://www.themealdb.com/api/json/v1/1/random.php")
                .build()
                .getAsString(object : StringRequestListener {

                    override fun onResponse(response: String?) {
                        response?.let { resp ->
                            val respObj = JSONObject(resp)
                            val meals = respObj.getJSONArray("meals")
                            val meal = meals.getJSONObject(0)

                            val ingredients = getIngredientsFromJson(meal)
                            val recipe = Recipe(
                                    meal.getInt("idMeal"),
                                    meal.getString("strMeal"),
                                    meal.getString("strCategory"),
                                    meal.getString("strTags"),
                                    meal.getString("strArea"),
                                    meal.getString("strInstructions"),
                                    ingredients[0],
                                    ingredients[1],
                                    meal.getString("strMealThumb"),
                                    meal.getString("strYoutube"),
                                    meal.getString("strSource"),
                                    System.currentTimeMillis()
                            )

                            mLatestRecipe.value?.takeIf { it.id == recipe.id }.let {
                                mRecipeDao.update(recipe)
                            }

                            mLatestRecipe.value?.takeUnless {  it.id == recipe.id }.let {
                                mRecipeDao.insert(recipe)
                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                    }

                })
    }

    private fun getIngredientsFromJson(meal: JSONObject): Array<String> {
        val ingredients = mutableListOf<String>()
        val measures = mutableListOf<String>()

        try {
            var counter = 1
            while (true) {
                val ingredient = meal.getString("strIngredient$counter")
                val measure = meal.getString("strMeasure$counter")

                if (ingredient.isBlank()) break

                ingredients.add(ingredient)
                measures.add(measure)

                counter++
            }
        } catch (e: JSONException) { }
        return arrayOf(
                ingredients.reduce { acc, str -> "$acc;$str" },
                measures.reduce { acc, str -> "$acc;$str" }
        )
    }

}