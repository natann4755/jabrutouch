package co.il.model.model.masechet_list_model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ChaptersItem implements Serializable {

	@SerializedName("chapter")
	private int chapter;

	@SerializedName("mishnayots")
	private int mishnayot;

	public void setChapter(int chapter){
		this.chapter = chapter;
	}

	public int getChapter(){
		return chapter;
	}

	public void setMishnayot(int mishnayot){
		this.mishnayot = mishnayot;
	}

	public int getMishnayot(){
		return mishnayot;
	}
}