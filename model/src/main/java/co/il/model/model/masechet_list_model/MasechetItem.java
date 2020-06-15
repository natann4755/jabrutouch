package co.il.model.model.masechet_list_model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MasechetItem implements Serializable {

	@SerializedName("pages")
	private int pages;

	@SerializedName("chapters_list")
	private List<ChaptersItem> chapters;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("chapters")
	private int chaptersCount;

	@SerializedName("order")
	private int order;

	public void setPages(int pages){
		this.pages = pages;
	}

	public int getPages(){
		return pages;
	}

	public void setChapters(List<ChaptersItem> chapters){
		this.chapters = chapters;
	}

	public List<ChaptersItem> getChapters(){
		return chapters;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setChaptersCount(int chaptersCount){
		this.chaptersCount = chaptersCount;
	}

	public int getChaptersCount(){
		return chaptersCount;
	}

	public void setOrder(int order){
		this.order = order;
	}

	public int getOrder(){
		return order;
	}
}