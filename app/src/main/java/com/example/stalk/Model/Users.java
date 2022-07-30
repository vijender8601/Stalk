package com.example.stalk.Model;

public class Users {
    public String emaill,fullName,password,codechefId,codeforceId,atcoderId,leetcodeId,profileImg;

    public String getCodechefId() {
        return codechefId;
    }

    public void setCodechefId(String codechefId) {
        this.codechefId = codechefId;
    }

    public String getCodeforceId() {
        return codeforceId;
    }

    public void setCodeforceId(String codeforceId) {
        this.codeforceId = codeforceId;
    }

    public Users(String emaill, String fullName, String password, String codechefId, String codeforceId) {
        this.emaill = emaill;
        this.fullName = fullName;
        this.password = password;
        this.codechefId = codechefId;
        this.codeforceId = codeforceId;
    }

    public Users(){

    }

    public Users(String emaill, String fullName, String password) {
        this.emaill = emaill;
        this.fullName = fullName;
        this.password = password;
    }

    public String getEmaill() {
        return emaill;
    }

    public void setEmaill(String emaill) {
        this.emaill = emaill;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAtcoderId() {
        return atcoderId;
    }

    public void setAtcoderId(String atcoderId) {
        this.atcoderId = atcoderId;
    }

    public String getLeetcodeId() {
        return leetcodeId;
    }

    public void setLeetcodeId(String leetcodeId) {
        this.leetcodeId = leetcodeId;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
