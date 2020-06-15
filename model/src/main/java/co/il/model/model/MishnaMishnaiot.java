package co.il.model.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MishnaMishnaiot implements Serializable {

	@SerializedName("mishnayot")
	private List<MishnayotItem> mishnayot;

	public void setMishnayot(List<MishnayotItem> mishnayot){
		this.mishnayot = mishnayot;
	}

	public List<MishnayotItem> getMishnayot(){
		return mishnayot;
	}


}