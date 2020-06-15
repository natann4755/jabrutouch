package co.il.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DedicationItem implements Serializable {

	@SerializedName("template")
	private String template;

	@SerializedName("id")
	private int id;

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}