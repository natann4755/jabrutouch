package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SocketDonateMessage{

	@SerializedName("watch_count")
	private int watchCount;

	@SerializedName("donated_by")
	private List<Integer> donatedBy;

	public void setWatchCount(int watchCount){
		this.watchCount = watchCount;
	}

	public int getWatchCount(){
		return watchCount;
	}

	public void setDonatedBy(List<Integer> donatedBy){
		this.donatedBy = donatedBy;
	}

	public List<Integer> getDonatedBy(){
		return donatedBy;
	}
}