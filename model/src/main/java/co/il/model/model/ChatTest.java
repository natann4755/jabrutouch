package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ChatTest{

	@SerializedName("data")
	private List<ChatObject> data;

	@SerializedName("success")
	private boolean success;

	public void setData(List<ChatObject> data){
		this.data = data;
	}

	public List<ChatObject> getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}
}