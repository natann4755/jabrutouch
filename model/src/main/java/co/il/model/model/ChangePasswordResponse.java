package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse{

	@SerializedName("status")
	private String status;

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}