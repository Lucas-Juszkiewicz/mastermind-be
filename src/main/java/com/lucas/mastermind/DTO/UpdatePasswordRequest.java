package com.lucas.mastermind.DTO;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String passOne;
    private String passTwo;
    private String passOld;
    private Long userId;

    public UpdatePasswordRequest(String passOne, String passTwo, String passOld, Long userId) {
        this.passOne = passOne;
        this.passTwo = passTwo;
        this.passOld = passOld;
        this.userId = userId;
    }

    public String getPassOne() {
        return passOne;
    }

    public void setPassOne(String passOne) {
        this.passOne = passOne;
    }

    public String getPassTwo() {
        return passTwo;
    }

    public void setPassTwo(String passTwo) {
        this.passTwo = passTwo;
    }

    public String getPassOld() {
        return passOld;
    }

    public void setPassOld(String passOld) {
        this.passOld = passOld;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
