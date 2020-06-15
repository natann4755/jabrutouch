package co.il.model.model;

import com.google.gson.annotations.SerializedName;


public class RequestValidationCode{

	@SerializedName("code")
	private String code;

	@SerializedName("phone")
	private String phone;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}


}