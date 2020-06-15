package co.il.model.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ChatObject implements Serializable {

	@SerializedName("chat_type")
	private int chatType;

	@SerializedName("to_user")
	private int toUser;

	@SerializedName("last_message_time")
	private Long lastMessageTime;

	@SerializedName("created_at")
	private Long createdAt;

	@SerializedName("messages")
	private List<MessageObject> messages;

	@SerializedName("last_message")
	private String lastMessage;

	@SerializedName("title")
	private String title;

	@SerializedName("chat_id")
	private int chatId;

	@SerializedName("from_user")
	private int fromUser;

	private int unreadMessages;

	private int lessonId;

	private boolean isGemara;

	public boolean getIsGemara() {
		return isGemara;
	}

	public void setIsGemara(boolean isGemara) {
		this.isGemara = isGemara;
	}

	public int getLessonId() {
		return lessonId;
	}

	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}

	public void setUnreadMessages(int unreadMessages) {
		this.unreadMessages = unreadMessages;
	}

	public int getUnreadMessages() {
		return unreadMessages;
	}

	public void setChatType(int chatType){
		this.chatType = chatType;
	}

	public int getChatType(){
		return chatType;
	}

	public void setToUser(int toUser){
		this.toUser = toUser;
	}

	public int getToUser(){
		return toUser;
	}

	public void setLastMessageTime(Long lastMessageTime){
		this.lastMessageTime = lastMessageTime;
	}

	public Long getLastMessageTime(){
		return lastMessageTime;
	}

	public void setCreatedAt(Long createdAt){
		this.createdAt = createdAt;
	}

	public Long getCreatedAt(){
		return createdAt;
	}

	public void setMessages(List<MessageObject> messages){
		this.messages = messages;
	}

	public List<MessageObject> getMessages(){
		return messages;
	}

	public void setLastMessage(String lastMessage){
		this.lastMessage = lastMessage;
	}

	public String getLastMessage(){
		return lastMessage;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setChatId(int chatId){
		this.chatId = chatId;
	}

	public int getChatId(){
		return chatId;
	}

	public void setFromUser(int fromUser){
		this.fromUser = fromUser;
	}

	public int getFromUser(){
		return fromUser;
	}
}