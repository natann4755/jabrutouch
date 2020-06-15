package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class DonatedByItem{

	@SerializedName("dedication_template_text")
	private String dedicationTemplateText;

	@SerializedName("name_to_represent")
	private String nameToRepresent;

	@SerializedName("dedication_text")
	private String dedicationText;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("country")
	private String country;

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setDedicationTemplateText(String dedicationTemplateText){
		this.dedicationTemplateText = dedicationTemplateText;
	}

	public String getDedicationTemplateText(){
		return dedicationTemplateText;
	}

	public void setNameToRepresent(String nameToRepresent){
		this.nameToRepresent = nameToRepresent;
	}

	public String getNameToRepresent(){
		return nameToRepresent;
	}

	public void setDedicationText(String dedicationText){
		this.dedicationText = dedicationText;
	}

	public String getDedicationText(){
		return dedicationText;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}
}