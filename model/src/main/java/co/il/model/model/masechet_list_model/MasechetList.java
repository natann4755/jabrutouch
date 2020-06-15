package co.il.model.model.masechet_list_model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MasechetList implements Serializable{

	@SerializedName("seder")
	private List<SederItem> seder;

	public void setSeder(List<SederItem> seder){
		this.seder = seder;
	}

	public List<SederItem> getSeder(){
		return seder;
	}


}