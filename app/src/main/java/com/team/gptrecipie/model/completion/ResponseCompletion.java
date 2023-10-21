package com.team.gptrecipie.model.completion;

import java.util.List;

public class ResponseCompletion {
    private String mId;
    private String mObject;
    private long mCreated;
    private String mModel;

    private List<ResponseChoice> mChoices;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getObject() {
        return mObject;
    }

    public void setObject(String object) {
        mObject = object;
    }

    public long getCreated() {
        return mCreated;
    }

    public void setCreated(long created) {
        mCreated = created;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public List<ResponseChoice> getChoices() {
        return mChoices;
    }

    public void setChoices(List<ResponseChoice> choices) {
        mChoices = choices;
    }
}
