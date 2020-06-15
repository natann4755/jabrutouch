package co.il.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Payment implements Serializable {

    @SerializedName("payment_type")
    private int paymentType;

    @SerializedName("dedication_template")
    private int dedicationTemplate;

    @SerializedName("name_to_represent")
    private String nameToRepresent;

    @SerializedName("dedication_text")
    private String dedicationText;

    @SerializedName("sum")
    private int sum;

    @SerializedName("status")
    private String status;

    @SerializedName("country")
    private String country;

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setDedicationTemplate(int dedicationTemplate) {
        this.dedicationTemplate = dedicationTemplate;
    }

    public int getDedicationTemplate() {
        return dedicationTemplate;
    }

    public void setNameToRepresent(String nameToRepresent) {
        this.nameToRepresent = nameToRepresent;
    }

    public String getNameToRepresent() {
        return nameToRepresent;
    }

    public void setDedicationText(String dedicationText) {
        this.dedicationText = dedicationText;
    }

    public String getDedicationText() {
        return dedicationText;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}