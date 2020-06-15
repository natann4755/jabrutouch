package co.il.model.model;

import com.google.gson.annotations.SerializedName;


public class MasechtotItem{

	@SerializedName("pages")
	private int pages;

	@SerializedName("masechet")
	private String masechet;

	public void setPages(int pages){
		this.pages = pages;
	}

	public int getPages(){
		return pages;
	}

	public void setMasechet(String masechet){
		this.masechet = masechet;
	}

	public String getMasechet(){
		return masechet;
	}
}