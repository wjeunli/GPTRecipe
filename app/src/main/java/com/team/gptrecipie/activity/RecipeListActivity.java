package com.team.gptrecipie.activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.team.gptrecipie.R;
import com.team.gptrecipie.fragment.RecipeFragment;
import com.team.gptrecipie.fragment.RecipeListFragment;
import com.team.gptrecipie.model.Recipe;


public class RecipeListActivity extends SingleFragmentActivity implements RecipeListFragment.Callbacks, RecipeFragment.CallBacks {
    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }

    /*@Override
    protected int getLayoutResId() {
        return R.layout.fragment_recipe_list;
    }*/

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = RecipePagerActivity.newCrimePagerActivityIntent(this, recipe.getId());
        startActivity(intent);
    }

    @Override
    public void onRecipeUpdated(Recipe recipe) {
        RecipeListFragment listFragment = (RecipeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
