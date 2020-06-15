package co.il.model.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;



public class PagesItem implements Serializable {

	@SerializedName("duration")
	private int duration;

	@SerializedName("chapter")
	private int chapter;

	@SerializedName("page_number")
	private int pageNumber;

	@SerializedName("presenter")
	private User presenter;

	@SerializedName("video_part")
	private List<VideoPart> videoPart;

	@SerializedName("id")
	private int gemaraId;

	@SerializedName("audio")
	private String audio;

	@SerializedName("video")
	private String video;

	@SerializedName("page")
	private String page;

	@SerializedName("gallery")
	private List<Gallery> gallery;

	private boolean downMode;
	private boolean deleteMode;
	private int masechetOrder;
	private int sederOrder;
	private String masechetName;
	private long timeLine;
	private String mediaType;
	private int audioProgress = -1;
	private int videoProgress = -1;
	private int position= -1;
	private int hasNewMessageFromRabbi = 0; // for recent pages in wall


	public int getHasNewMessageFromRabbi() {
		return hasNewMessageFromRabbi;
	}


	public void setHasNewMessageFromRabbi(int hasNewMessageFromRabbi) {
		this.hasNewMessageFromRabbi = hasNewMessageFromRabbi;
	}

	public void setDeleteMode(boolean deleteMode) {
		this.deleteMode = deleteMode;
	}

	public boolean isDeleteMode() {
		return deleteMode;
	}

	public int getPosition() {
		return position;
	}


	public void setPosition(int position) {
		this.position = position;
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

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setTimeLine(long timeLine) {
		this.timeLine = timeLine;
	}

	public long getTimeLine() {
		return timeLine;
	}

	public void setMasechetName(String masechetName) {
		this.masechetName = masechetName;
	}

	public String getMasechetName() {
		return masechetName;
	}

	public void setGemaraId(int gemaraId) {
		this.gemaraId = gemaraId;
	}

	public void setMasechetOrder(int masechetOrder) {
		this.masechetOrder = masechetOrder;
	}

	public void setSederOrder(int sederOrder) {
		this.sederOrder = sederOrder;
	}

	public int getGemaraId() {
		return gemaraId;
	}

	public int getMasechetOrder() {
		return masechetOrder;
	}

	public int getSederOrder() {
		return sederOrder;
	}

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

	public void setPageNumber(int pageNumber){
		this.pageNumber = pageNumber;
	}

	public int getPageNumber(){
		return pageNumber;
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

	public void setId(int id){
		this.gemaraId = id;
	}

	public int getId(){
		return gemaraId;
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

	public void setPage(String page){
		this.page = page;
	}

	public String getPage(){
		return page;
	}

	public void setGallery(List<Gallery> gallery){
		this.gallery = gallery;
	}

	public List<Gallery> getGallery(){
		return gallery;
	}
}