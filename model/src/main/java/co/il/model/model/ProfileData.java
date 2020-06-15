package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProfileData {

	@SerializedName("Communities")
	private List<IdAndNameDetailed> communities;

	@SerializedName("Education")
	private List<IdAndNameDetailed> education;

	@SerializedName("Interest")
	private List<IdAndNameDetailed> interest;

	@SerializedName("Occupation")
	private List<IdAndNameDetailed> occupation;

	public void setCommunities(List<IdAndNameDetailed> communities){
		this.communities = communities;
	}

	public List<IdAndNameDetailed> getCommunities(){
		return communities;
	}

	public void setEducation(List<IdAndNameDetailed> education){
		this.education = education;
	}

	public List<IdAndNameDetailed> getEducation(){
		return education;
	}

	public void setInterest(List<IdAndNameDetailed> interest){
		this.interest = interest;
	}

	public List<IdAndNameDetailed> getInterest(){
		return interest;
	}

	public void setOccupation(List<IdAndNameDetailed> occupation){
		this.occupation = occupation;
	}

	public List<IdAndNameDetailed> getOccupation(){
		return occupation;
	}
}