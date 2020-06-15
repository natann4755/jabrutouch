package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("error_type")
	private String errorType;

	@SerializedName("error_code")
	private Integer errorCode;

	@SerializedName("errors")
	private List<ErrorsItem> errors;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setErrorType(String errorType){
		this.errorType = errorType;
	}

	public String getErrorType(){
		return errorType;
	}

	public void setErrorCode(int errorCode){
		this.errorCode = errorCode;
	}

	public int getErrorCode(){
		return errorCode;
	}

	public void setErrors(List<ErrorsItem> errors){
		this.errors = errors;
	}

	public List<ErrorsItem> getErrors(){
		return errors;
	}
}