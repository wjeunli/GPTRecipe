package com.team.gptrecipie.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.team.gptrecipie.dao.RecipeDbSchema.RecipeTable;

class RecipeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "recipeBase.db";

    public RecipeBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RecipeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                RecipeTable.Columns.UUID + ", " +
                RecipeTable.Columns.TITLE + ", " +
                RecipeTable.Columns.DATE + ", " +
                RecipeTable.Columns.DELICIOUS + ", " +
                RecipeTable.Columns.INGREDIENT + ", " +
                RecipeTable.Columns.CONTENT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
