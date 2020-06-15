package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class ResponseValidation extends GenericResponse{

	@SerializedName("phone")
	private String phone;

	@SerializedName("id")
	private int id;

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}