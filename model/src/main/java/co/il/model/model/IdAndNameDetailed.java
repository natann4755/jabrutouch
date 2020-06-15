package co.il.model.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IdAndNameDetailed implements Parcelable, Serializable {

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	private boolean isSelected;

	public IdAndNameDetailed(String name, int id) {
		this.name = name;
		this.id = id;
	}

	protected IdAndNameDetailed(Parcel in) {
		name = in.readString();
		id = in.readInt();
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(name);
		parcel.writeInt(id);
	}


	public static final Creator<IdAndNameDetailed> CREATOR = new Creator<IdAndNameDetailed>() {
		@Override
		public IdAndNameDetailed createFromParcel(Parcel in) {
			return new IdAndNameDetailed(in);
		}

		@Override
		public IdAndNameDetailed[] newArray(int size) {
			return new IdAndNameDetailed[size];
		}
	};

}