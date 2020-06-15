package co.il.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoPart implements Serializable {

	@SerializedName("video_part_time_line")
	private String videoPartTimeLine;

	@SerializedName("part_title")
	private String partTitle;

	@SerializedName("id")
	private int id;

	public void setVideoPartTimeLine(String videoPartTimeLine){
		this.videoPartTimeLine = videoPartTimeLine;
	}

	public String getVideoPartTimeLine(){
		return videoPartTimeLine;
	}

	public void setPartTitle(String partTitle){
		this.partTitle = partTitle;
	}

	public String getPartTitle(){
		return partTitle;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}