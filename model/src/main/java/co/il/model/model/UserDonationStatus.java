package co.il.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDonationStatus implements Serializable {

	@SerializedName("all_crowns")
	private int allCrowns;

	@SerializedName("unused_crowns")
	private int unusedCrowns;

	@SerializedName("likes")
	private int likes;

	@SerializedName("watch_count")
	private int watchCount;

	@SerializedName("donate_per_month")
	private int donatePerMonth;



	public int getDonatePerMonth() {
		return donatePerMonth;
	}

	public void setDonatePerMonth(int donatePerMonth) {
		this.donatePerMonth = donatePerMonth;
	}

	public int getWatchCount() {
		return watchCount;
	}

	public void setWatchCount(int watchCount) {
		this.watchCount = watchCount;
	}

	public void setAllCrowns(int allCrowns){
		this.allCrowns = allCrowns;
	}

	public int getAllCrowns(){
		return allCrowns;
	}

	public void setUnusedCrowns(int unusedCrowns){
		this.unusedCrowns = unusedCrowns;
	}

	public int getUnusedCrowns(){
		return unusedCrowns;
	}

	public void setLikes(int likes){
		this.likes = likes;
	}

	public int getLikes(){
		return likes;
	}
}