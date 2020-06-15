package co.il.model.model.masechet_list_model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SederItem implements Serializable {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("masechet")
	private List<MasechetItem> masechet;

	@SerializedName("order")
	private int order;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMasechet(List<MasechetItem> masechet){
		this.masechet = masechet;
	}

	public List<MasechetItem> getMasechet(){
		return masechet;
	}

	public void setOrder(int order){
		this.order = order;
	}

	public int getOrder(){
		return order;
	}
}