package me.inibukanadit.randomrecipe.app

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.item_list_ingredient.view.*

class RecipeActivity : AppCompatActivity() {

    private val mViewModel by lazy { ViewModelProviders.of(this).get(RecipeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        mViewModel.getLatestRecipe().observe(this, Observer { recipe ->
            srl_recipe_refresh.isRefreshing = false

            recipe?.let {
                Picasso.get().load(it.thumbUrl).into(iv_recipe_thumbnail)

                val ingredients = it.ingredients.split(";")
                val measures = it.measures.split(";")

                tl_recipe_ingredients.removeAllViews()

                for (index in 0 until ingredients.size) {
                    val row = layoutInflater.inflate(R.layout.item_list_ingredient, tl_recipe_ingredients, false)

                    row.tv_recipe_item_number.text = (index + 1).toString()
                    row.tv_recipe_item_name.text = ingredients[index]
                    row.tv_recipe_item_measure.text = measures[index]

                    tl_recipe_ingredients.addView(row)
                }

                tv_recipe_name.text = it.name
                tv_recipe_information.text = "${it.area} - ${it.category}"
                tv_recipe_instruction.text = it.instructions
            }
        })

        srl_recipe_refresh.setOnRefreshListener {
            mViewModel.fetchRandomRecipe()
        }
    }

}
