package com.team.gptrecipie.model;

import java.util.Date;

public class RecipeNew {
    private String ingredients;
    private long id;
    private String mContent;

    public String[] getIngredientList() {
        return ingredients == null ? null : ingredients.split(",");
    }
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    private Date mDate ;



}
