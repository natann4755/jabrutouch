package co.il.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CrownsItem implements Serializable {

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("price")
	private int dollarPerCrown;

	@SerializedName("id")
	private int id;

	public void setPaymentType(String paymentType){
		this.paymentType = paymentType;
	}

	public String getPaymentType(){
		return paymentType;
	}

	public void setDollarPerCrown(int dollarPerCrown){
		this.dollarPerCrown = dollarPerCrown;
	}

	public int getDollarPerCrown(){
		return dollarPerCrown;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}