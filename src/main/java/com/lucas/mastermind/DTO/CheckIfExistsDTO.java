package com.lucas.mastermind.DTO;

public class CheckIfExistsDTO {

    private Boolean doesExists;

    public CheckIfExistsDTO(Boolean doesExists) {
        this.doesExists = doesExists;
    }

    public Boolean getDoesExists() {
        return doesExists;
    }

    public void setDoesExists(Boolean doesExists) {
        this.doesExists = doesExists;
    }
}
