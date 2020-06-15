package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LessonDonationBy{

	@SerializedName("crown_id")
	private int crownId;

	@SerializedName("donated_by")
	private List<DonatedByItem> donatedBy;

	public void setDonatedBy(List<DonatedByItem> donatedBy){
		this.donatedBy = donatedBy;
	}

	public List<DonatedByItem> getDonatedBy(){
		return donatedBy;
	}


	public int getCrownId() {
		return crownId;
	}

	public void setCrownId(int crownId) {
		this.crownId = crownId;
	}
}