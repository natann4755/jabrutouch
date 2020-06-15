package co.il.model.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Chats{

	@SerializedName("chats")
	private List<ChatObject> chats;

	public void setChats(List<ChatObject> chats){
		this.chats = chats;
	}

	public List<ChatObject> getChats(){
		return chats;
	}
}