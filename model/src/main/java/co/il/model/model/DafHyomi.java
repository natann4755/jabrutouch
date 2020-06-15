package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import co.il.model.model.DafYomiDetails;


public class DafHyomi{

	@SerializedName("seder")
	private List<SederItem> seder;

	@SerializedName("dafYomiDetails")
	private DafYomiDetails dafYomiDetails;

	public void setSeder(List<SederItem> seder){
		this.seder = seder;
	}

	public List<SederItem> getSeder(){
		return seder;
	}

	public void setDafYomiDetails(DafYomiDetails dafYomiDetails){
		this.dafYomiDetails = dafYomiDetails;
	}

	public DafYomiDetails getDafYomiDetails(){
		return dafYomiDetails;
	}
}