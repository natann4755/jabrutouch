package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class GenericResponse{

	@SerializedName("success")
	private boolean success;


	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}
}