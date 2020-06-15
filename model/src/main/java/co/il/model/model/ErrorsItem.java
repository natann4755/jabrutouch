package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class ErrorsItem{

	@SerializedName("field")
	private String field;

	@SerializedName("message")
	private String message;

	public void setField(String field){
		this.field = field;
	}

	public String getField(){
		return field;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}