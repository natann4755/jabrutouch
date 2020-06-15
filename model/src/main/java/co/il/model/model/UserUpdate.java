package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserUpdate{

	@SerializedName("birthday")
	private String birthday;

	@SerializedName("image")
	private String image;

	@SerializedName("country")
	private String country;

	@SerializedName("education_id")
	private Integer educationId;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("interest_id")
	private List<Integer> interestId;

	@SerializedName("community_id")
	private Integer communityId;

	@SerializedName("phone")
	private String phone;

	@SerializedName("is_presenter")
	private boolean isPresenter;

	@SerializedName("second_email")
	private String secondEmail;

	@SerializedName("occupation_id")
	private Integer occupationId;

	@SerializedName("id")
	private int id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("religious_level")
	private Integer religiousLevel;

	@SerializedName("email")
	private String email;


	public void setBirthday(String birthday){
		this.birthday = birthday;
	}

	public String getBirthday(){
		return birthday;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setEducationId(Integer educationId){
		this.educationId = educationId;
	}

	public Integer getEducationId(){
		return educationId;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setInterestId(List<Integer> interestId){
		this.interestId = interestId;
	}

	public List<Integer> getInterestId(){
		return interestId;
	}

	public void setCommunityId(Integer communityId){
		this.communityId = communityId;
	}

	public Integer getCommunityId(){
		return communityId;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setIsPresenter(boolean isPresenter){
		this.isPresenter = isPresenter;
	}

	public boolean isIsPresenter(){
		return isPresenter;
	}

	public void setSecondEmail(String secondEmail){
		this.secondEmail = secondEmail;
	}

	public String getSecondEmail(){
		return secondEmail;
	}

	public void setOccupationId(Integer occupationId){
		this.occupationId = occupationId;
	}

	public Integer getOccupationId(){
		return occupationId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setReligiousLevel(Integer religiousLevel){
		this.religiousLevel = religiousLevel;
	}

	public Integer getReligiousLevel(){
		return religiousLevel;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}