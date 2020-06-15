package co.il.model.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MishnayotItem implements Serializable {

	@SerializedName("duration")
	private int duration;

	@SerializedName("chapter")
	private int chapter;

	@SerializedName("presenter")
	private User presenter;

	@SerializedName("video_part")
	private List<VideoPart> videoPart;

	@SerializedName("mishna")
	private int mishna;

	@SerializedName("id")
	private int id;

	@SerializedName("audio")
	private String audio;

	private String page;

	private int audioProgress = -1;
	private int videoProgress = -1;

	private int position = -1;
	private boolean deleteMode;
	private int hasNewMessageFromRabbi = 0; // for recent pages in wall


	public int getHasNewMessageFromRabbi() {
		return hasNewMessageFromRabbi;
	}


	public void setHasNewMessageFromRabbi(int hasNewMessageFromRabbi) {
		this.hasNewMessageFromRabbi = hasNewMessageFromRabbi;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setDeleteMode(boolean deleteMode) {
		this.deleteMode = deleteMode;
	}

	public boolean isDeleteMode() {
		return deleteMode;
	}

	public void setAudioProgress(int audioProgress) {
		this.audioProgress = audioProgress;
	}

	public void setVideoProgress(int videoProgress) {
		this.videoProgress = videoProgress;
	}

	public int getAudioProgress() {
		return audioProgress;
	}

	public int getVideoProgress() {
		return videoProgress;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return page;
	}

	public void setMasechetOrder(int masechetOrder) {
        this.masechetOrder = masechetOrder;
    }

    public void setSederOrder(int sederOrder) {
        this.sederOrder = sederOrder;
    }

    public void setMasechetName(String masechetName) {
        this.masechetName = masechetName;
    }

    public void setTimeLine(long timeLine) {
        this.timeLine = timeLine;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getMasechetOrder() {
        return masechetOrder;
    }

    public int getSederOrder() {
        return sederOrder;
    }

    public String getMasechetName() {
        return masechetName;
    }

    public long getTimeLine() {
        return timeLine;
    }

    public String getMediaType() {
        return mediaType;
    }

    @SerializedName("video")
	private String video;

	@SerializedName("gallery")
	private List<Gallery> gallery;

    private int masechetOrder;
    private int sederOrder;
    private String masechetName;
    private long timeLine;
    private String mediaType;

	private boolean downMode;

	public void setDownMode(boolean downMode) {
		this.downMode = downMode;
	}

	public boolean isDownMode() {
		return downMode;
	}

	public void setDuration(int duration){
		this.duration = duration;
	}

	public int getDuration(){
		return duration;
	}

	public void setChapter(int chapter){
		this.chapter = chapter;
	}

	public int getChapter(){
		return chapter;
	}

	public void setPresenter(User presenter){
		this.presenter = presenter;
	}

	public User getPresenter(){
		return presenter;
	}

	public void setVideoPart(List<VideoPart> videoPart){
		this.videoPart = videoPart;
	}

	public List<VideoPart> getVideoPart(){
		return videoPart;
	}

	public void setMishna(int mishna){
		this.mishna = mishna;
	}

	public int getMishna(){
		return mishna;
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

	public void setGallery(List<Gallery> gallery){
		this.gallery = gallery;
	}

	public List<Gallery> getGallery(){
		return gallery;
	}


}