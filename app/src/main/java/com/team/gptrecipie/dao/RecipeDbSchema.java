package com.team.gptrecipie.dao;

class RecipeDbSchema {
    public static final class RecipeTable {
        public static final String NAME = "recipes";

        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DELICIOUS = "delicious";
            public static final String CONTENT = "suspect";
            public static final String INGREDIENT = "ingredient";

        }
    }
}
