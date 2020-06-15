package co.il.model.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Media implements Serializable {

	@SerializedName("video_part")
	private List<Object> videoPart;

	@SerializedName("id")
	private int id;

	@SerializedName("audio")
	private String audio;

	@SerializedName("video")
	private String video;

	public void setVideoPart(List<Object> videoPart){
		this.videoPart = videoPart;
	}

	public List<Object> getVideoPart(){
		return videoPart;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAudio(String audio){
		this.audio = audio;
	}

	public String getAudio(){
		return audio;
	}

	public void setVideo(String video){
		this.video = video;
	}

	public String getVideo(){
		return video;
	}
}