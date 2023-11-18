package com.cahcet.FinalProject.web.dto;

public class UserKeyDto {
    private String key;

    public UserKeyDto(String key) {
        this.key = key;
    }

    public UserKeyDto() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
