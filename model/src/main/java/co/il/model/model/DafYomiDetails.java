package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class DafYomiDetails{

	@SerializedName("allPages")
	private int allPages;

	@SerializedName("dafStart")
	private int dafStart;

	@SerializedName("Kinnim")
	private int kinnim;

	@SerializedName("Midot")
	private int Midot;

	@SerializedName("dateStart")
	private String dateStart;

	@SerializedName("Tamid")
	private int tamid;


	public void setMidot(int midot) {
		Midot = midot;
	}

	public int getMidot() {
		return Midot;
	}

	public void setAllPages(int allPages){
		this.allPages = allPages;
	}

	public int getAllPages(){
		return allPages;
	}

	public void setDafStart(int dafStart){
		this.dafStart = dafStart;
	}

	public int getDafStart(){
		return dafStart;
	}

	public void setKinnim(int kinnim){
		this.kinnim = kinnim;
	}

	public int getKinnim(){
		return kinnim;
	}

	public void setDateStart(String dateStart){
		this.dateStart = dateStart;
	}

	public String getDateStart(){
		return dateStart;
	}

	public void setTamid(int tamid){
		this.tamid = tamid;
	}

	public int getTamid(){
		return tamid;
	}
}