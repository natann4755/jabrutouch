package co.il.model.model;

import com.google.gson.annotations.SerializedName;

public class AnalyticsData{

	@SerializedName("duration")
	private long duration;

	@SerializedName("page_id")
	private String pageId;

	@SerializedName("media_type")
	private String mediaType;

	@SerializedName("online")
	private int online;

	@SerializedName("event")
	private String event;

	@SerializedName("category")
	private String category;

	public static final String WATCH = "watch";
	public static final String DOWNLOAD = "download";
	public static final String DELETE = "delete";

	public static final String GEMARA = "Gemara";
	public static final String MISHNA = "Mishna";

	public static final String AUDIO = "audio";
	public static final String VIDEO = "video";


	private long timeStart;
	private boolean isPlaying;

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean playing) {
		isPlaying = playing;
	}

	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public void setDuration(long duration){
		this.duration = duration;
	}

	public long getDuration(){
		return duration;
	}

	public void setPageId(String pageId){
		this.pageId = pageId;
	}

	public String getPageId(){
		return pageId;
	}

	public void setMediaType(String mediaType){
		this.mediaType = mediaType;
	}

	public String getMediaType(){
		return mediaType;
	}

	public void setOnline(int online){
		this.online = online;
	}

	public int getOnline(){
		return online;
	}

	public void setEvent(String event){
		this.event = event;
	}

	public String getEvent(){
		return event;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}
}