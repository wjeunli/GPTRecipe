package com.team.gptrecipie.api.completion;

import java.util.List;

public class ResponseCompletion {
    private String id;
    private String object;
    private long created;
    private String model;

    private List<ResponseChoice> choices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ResponseChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ResponseChoice> choices) {
        this.choices = choices;
    }
}
