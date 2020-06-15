package co.il.model.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GemaraPages implements Serializable {

	@SerializedName("pages")
	private List<PagesItem> pages;

	public void setPages(List<PagesItem> pages){
		this.pages = pages;
	}

	public List<PagesItem> getPages(){
		return pages;
	}
}