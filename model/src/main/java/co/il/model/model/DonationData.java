package co.il.model.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DonationData implements Serializable {

	@SerializedName("crowns")
	private List<CrownsItem> crowns;

	@SerializedName("dedication_templates")
	private List<DedicationItem> dedication;

	public void setCrowns(List<CrownsItem> crowns){
		this.crowns = crowns;
	}

	public List<CrownsItem> getCrowns(){
		return crowns;
	}

	public void setDedication(List<DedicationItem> dedication){
		this.dedication = dedication;
	}

	public List<DedicationItem> getDedication(){
		return dedication;
	}
}