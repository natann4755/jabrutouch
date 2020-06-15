package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class LessonLike{

	@SerializedName("crown_id")
	private int crownId;

	@SerializedName("is_gemara")
	private boolean isGemara;

	@SerializedName("lesson_id")
	private int lessonId;

	public int getCrownId() {
		return crownId;
	}

	public void setCrownId(int crownId) {
		this.crownId = crownId;
	}

	public void setIsGemara(boolean isGemara){
		this.isGemara = isGemara;
	}

	public boolean isIsGemara(){
		return isGemara;
	}

	public void setLessonId(int lessonId){
		this.lessonId = lessonId;
	}

	public int getLessonId(){
		return lessonId;
	}
}