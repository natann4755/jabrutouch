package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class SederItem{

	@SerializedName("masechtot")
	private List<MasechtotItem> masechtot;

	@SerializedName("name")
	private String name;

	public void setMasechtot(List<MasechtotItem> masechtot){
		this.masechtot = masechtot;
	}

	public List<MasechtotItem> getMasechtot(){
		return masechtot;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}