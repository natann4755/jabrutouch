package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class RequestSendPhoneNUmber {

	@SerializedName("phone")
	private String phone;

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}
}