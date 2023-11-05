package com.team.gptrecipie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.team.gptrecipie.R;
import com.team.gptrecipie.dao.RecipeDAO;
import com.team.gptrecipie.fragment.RecipeFragment;
import com.team.gptrecipie.fragment.RecipeListFragment;
import com.team.gptrecipie.model.Recipe;

import java.util.List;
import java.util.UUID;

public class RecipePagerActivity extends AppCompatActivity implements RecipeFragment.CallBacks {

    public static final String EXTRA_RECIPE_ID = "com.team.gptrecipie.activity.RecipePagerActivity.recipe_id";
    public static final int REQUEST_CRIME = 2;
    private ViewPager mViewPager;
    private List<Recipe> mRecipes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_pager);
        FragmentManager fm = getSupportFragmentManager();
        mRecipes = RecipeDAO.get(this).getRecipes();

        mViewPager = findViewById(R.id.recipe_view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Recipe recipe = mRecipes.get(position);
                return RecipeFragment.newInstance(recipe.getId());
            }

            @Override
            public int getCount() {
                return mRecipes.size();
            }
        });

        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        for (int i = 0; i < mRecipes.size(); i++) {
            if (uuid.equals(mRecipes.get(i).getId())) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onRecipeUpdated(Recipe recipe) {

    }

    public static Intent newCrimePagerActivityIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, RecipePagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, uuid);
        return intent;
    }
}
