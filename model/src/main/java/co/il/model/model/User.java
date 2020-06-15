package co.il.model.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class User implements Serializable {

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("image")
    private String image;

    @SerializedName("country")
    private String country;

    @SerializedName("education")
    private IdAndNameDetailed education;

    @SerializedName("occupation")
    private IdAndNameDetailed occupation;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("community")
    private IdAndNameDetailed community;

    @SerializedName("token")
    private String token;

    @SerializedName("phone")
    private String phone;

    @SerializedName("interest")
    private List<IdAndNameDetailed> interest;

    @SerializedName("is_presenter")
    private boolean isPresenter;

    @SerializedName("second_email")
    private String secondEmail;

    @SerializedName("id")
    private int id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("religious_level")
    private int religiousLevel;

    @SerializedName("email")
    private String email;

    @SerializedName("profile_percent")
    private int profilePercent;

    @SerializedName("lesson_watch_count")
    private int lessonWatchCount;


    @SerializedName("lesson_donated")
    private LessonDonatedObject lessonDonatedObject;

    public LessonDonatedObject getLessonDonatedObject() {
        return lessonDonatedObject;
    }

    public void setLessonDonatedObject(LessonDonatedObject lessonDonatedObject) {
        this.lessonDonatedObject = lessonDonatedObject;
    }

    public int getLessonWatchCount() {
        return lessonWatchCount;
    }

    public void setLessonWatchCount(int lessonWatchCount) {
        this.lessonWatchCount = lessonWatchCount;
    }

    public int getProfilePercent() {
        return profilePercent;
    }

    public void setProfilePercent(int profilePercent) {
        this.profilePercent = profilePercent;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setEducation(IdAndNameDetailed education) {
        this.education = education;
    }

    public IdAndNameDetailed getEducation() {
        return education;
    }

    public void setOccupation(IdAndNameDetailed occupation) {
        this.occupation = occupation;
    }

    public IdAndNameDetailed getOccupation() {
        return occupation;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCommunity(IdAndNameDetailed community) {
        this.community = community;
    }

    public IdAndNameDetailed getCommunity() {
        return community;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setInterest(List<IdAndNameDetailed> interest) {
        this.interest = interest;
    }

    public List<IdAndNameDetailed> getInterest() {
        return interest;
    }

    public void setIsPresenter(boolean isPresenter) {
        this.isPresenter = isPresenter;
    }

    public boolean isIsPresenter() {
        return isPresenter;
    }

    public void setSecondEmail(String secondEmail) {
        this.secondEmail = secondEmail;
    }

    public String getSecondEmail() {
        return secondEmail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setReligiousLevel(int religiousLevel) {
        this.religiousLevel = religiousLevel;
    }

    public int getReligiousLevel() {
        return religiousLevel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}