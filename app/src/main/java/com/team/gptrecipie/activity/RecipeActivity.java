package com.team.gptrecipie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import com.team.gptrecipie.fragment.RecipeFragment;
import com.team.gptrecipie.model.Recipe;

import java.util.UUID;

public class RecipeActivity extends SingleFragmentActivity {

    public static final String EXTRA_RECIPE_ID = "com.team.gptrecipie.recipe_id";
    public static final int REQUEST_CRIME = 1;
    private Recipe mRecipe;

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_RECIPE_ID);
        return RecipeFragment.newInstance(uuid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent newCrimeActivityIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, uuid);
        return intent;
    }


    /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/
}