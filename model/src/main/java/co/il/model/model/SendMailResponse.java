package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class SendMailResponse{

	@SerializedName("user_exist_status")
	private boolean userExistStatus;

	@SerializedName("message")
	private String message;

	public void setUserExistStatus(boolean userExistStatus){
		this.userExistStatus = userExistStatus;
	}

	public boolean isUserExistStatus(){
		return userExistStatus;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}