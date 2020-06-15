package co.il.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LessonDonatedObject implements Serializable {

	@SerializedName("donated")
	private boolean donated;

	@SerializedName("lesson_donated")
	private int lessonDonated;

	public void setDonated(boolean donated){
		this.donated = donated;
	}

	public boolean isDonated(){
		return donated;
	}

	public void setLessonDonated(int lessonDonated){
		this.lessonDonated = lessonDonated;
	}

	public int getLessonDonated(){
		return lessonDonated;
	}
}