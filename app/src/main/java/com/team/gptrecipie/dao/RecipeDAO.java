package com.team.gptrecipie.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;


import com.team.gptrecipie.model.Recipe;
import com.team.gptrecipie.dao.RecipeDbSchema.RecipeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class RecipeDAO {
    private static RecipeDAO sRecipeDAO;
    private Map<UUID, Recipe> mRecipes = new HashMap();
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private ContentValues getContentValue(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeTable.Columns.UUID, recipe.getId().toString());
        values.put(RecipeTable.Columns.TITLE, recipe.getTitle());
        values.put(RecipeTable.Columns.DATE, recipe.getDate().getTime());
        values.put(RecipeTable.Columns.DELICIOUS, recipe.isDelicious() ? 1 : 0);
        values.put(RecipeTable.Columns.CONTENT, recipe.getContent());
        values.put(RecipeTable.Columns.INGREDIENT, recipe.getIngredient());

        return values;
    }

    private RecipeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RecipeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new RecipeCursorWrapper(cursor);
    }

    class RecipeCursorWrapper extends CursorWrapper {
        public RecipeCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Recipe getRecipe() {
            String uuid = getString(getColumnIndex(RecipeTable.Columns.UUID));
            String title = getString(getColumnIndex(RecipeTable.Columns.TITLE));
            long date = getLong(getColumnIndex(RecipeTable.Columns.DATE));
            int solved = getInt(getColumnIndex(RecipeTable.Columns.DELICIOUS));
            String suspect = getString(getColumnIndex(RecipeTable.Columns.CONTENT));

            Recipe recipe = new Recipe(UUID.fromString(uuid));
            recipe.setTitle(title);
            recipe.setDate(new Date(date));
            recipe.setDelicious(solved != 0);
            recipe.setContent(suspect);

            return recipe;
        }
    }

    public static RecipeDAO get(Context context) {
        if (sRecipeDAO == null) {
            sRecipeDAO = new RecipeDAO(context);
        }
        return sRecipeDAO;
    }

    private RecipeDAO(Context context) {
        //addFakeCrimes(50);
        mContext = context.getApplicationContext();
        mDatabase = new RecipeBaseHelper(context).getWritableDatabase();
    }

    Random mRandom = new Random(System.currentTimeMillis());

    private void addFakeRecipe(int size) {
        for (int i = 0; i < size; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe_#" + i);
            recipe.setDelicious(mRandom.nextBoolean());
            mRecipes.put(recipe.getId(), recipe);
        }
    }

    public File getPhotoFile(Recipe recipe) {
        File fileDir = mContext.getFilesDir();

        return new File(fileDir, recipe.getPhotoFileName());
    }

    public void addRecipe(Recipe recipe) {
        // mCrimes.put(crime.getId(), crime);
        ContentValues values = getContentValue(recipe);
        mDatabase.insert(RecipeTable.NAME, null, values);
    }

    public void updateRecipe(Recipe recipe) {
        String uuid = recipe.getId().toString();
        ContentValues values = getContentValue(recipe);

        mDatabase.update(RecipeTable.NAME, values,
                RecipeTable.Columns.UUID + " = ?",
                new String[]{uuid}
        );
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        RecipeCursorWrapper cursor = queryCrimes(null, null);

        try {
            if (cursor.moveToNext()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    recipes.add(cursor.getRecipe());
                }
            }

        } finally {
            cursor.close();
        }
        return recipes;
    }

    public Recipe getRecipe(UUID uuid) {
        RecipeCursorWrapper cursor = queryCrimes(
                RecipeTable.Columns.UUID + " = ?",
                new String[]{uuid.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRecipe();
        } finally {
            cursor.close();
        }

    }
}
