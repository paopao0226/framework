package org.csu.mypetstore.domain;

import java.io.Serializable;

public class Account implements Serializable {

    private static final long serialVersionUID = 8751282105532159742L;

    private String userId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private String addr1;
    private String addr2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String favouriteCategoryId;
    private String languagePreference;
    private boolean listOption;
    private boolean bannerOption;
    private String bannerName;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String role;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return userId;
    }

    public void setUsername(String username) {
        this.userId = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress1() {
        return addr1;
    }

    public void setAddress1(String address1) {
        this.addr1 = address1;
    }

    public String getAddress2() {
        return addr2;
    }

    public void setAddress2(String address2) {
        this.addr2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFavouriteCategoryId() {
        return favouriteCategoryId;
    }

    public void setFavouriteCategoryId(String favouriteCategoryId) {
        this.favouriteCategoryId = favouriteCategoryId;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public boolean isListOption() {
        return listOption;
    }

    public void setListOption(boolean listOption) {
        this.listOption = listOption;
    }

    public boolean isBannerOption() {
        return bannerOption;
    }

    public void setBannerOption(boolean bannerOption) {
        this.bannerOption = bannerOption;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }
}
