package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class ChangePassword{

	@SerializedName("old_password")
	private String oldPassword;

	@SerializedName("new_password")
	private String newPassword;

	public void setOldPassword(String oldPassword){
		this.oldPassword = oldPassword;
	}

	public String getOldPassword(){
		return oldPassword;
	}

	public void setNewPassword(String newPassword){
		this.newPassword = newPassword;
	}

	public String getNewPassword(){
		return newPassword;
	}
}