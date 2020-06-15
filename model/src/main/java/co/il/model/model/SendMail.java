package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class SendMail{

	@SerializedName("email")
	private String email;

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}