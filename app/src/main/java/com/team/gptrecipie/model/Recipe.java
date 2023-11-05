package com.team.gptrecipie.model;

import java.util.Date;
import java.util.UUID;

public class Recipe {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mDelicious;
    private String mContent;
    private String mIngredient;

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }

    public Recipe() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }
    public Recipe(UUID uuid) {
        mId = uuid;
    }

    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getTitle() {
        return mTitle;
    }

    public UUID getId() {
        return mId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isDelicious() {
        return mDelicious;
    }

    public void setDelicious(boolean delicious) {
        mDelicious = delicious;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
