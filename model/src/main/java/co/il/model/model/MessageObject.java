package co.il.model.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageObject implements Serializable {

    @SerializedName("sent_at")
    private Long sentAt;

    @SerializedName("to_user")
    private int toUser;

    @SerializedName("is_mine")
    private boolean isMine;

    @SerializedName("read")
    private boolean read;

    @SerializedName("message_id")
    private int messageId;

    @SerializedName("message_type")
    private int messageType;

    @SerializedName("message")
    private String message;

    @SerializedName("title")
    private String title;

    @SerializedName("from_user")
    private int fromUser;

    @SerializedName("chat_id")
    private int chatId;

    @SerializedName("new_chat")
    private boolean isNewChat;

    @SerializedName("image")
    private String image;

   @SerializedName("lesson_id")
    private Integer lessonId;

    @SerializedName("gemara")
    private boolean isGemara;

    @SerializedName("link_to")
    private int linkTo;



    public int getLinkTo() {
        return linkTo;
    }

    public void setLinkTo(int linkTo) {
        this.linkTo = linkTo;
    }

    public boolean isGemara() {
        return isGemara;
    }

    public void setIsGemara(boolean gemara) {
        isGemara = gemara;
    }

    public Integer getLessonId() {
        return lessonId;
    }


    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setNewChat(boolean newChat) {
        isNewChat = newChat;
    }


    public boolean isNewChat() {
        return isNewChat;
    }

    public void setSentAt(Long sentAt){
        this.sentAt = sentAt;
    }

    public Long getSentAt(){
        return sentAt;
    }

    public void setToUser(int toUser){
        this.toUser = toUser;
    }

    public int getToUser(){
        return toUser;
    }

    public void setIsMine(boolean isMine){
        this.isMine = isMine;
    }

    public boolean getIsMine(){
        return isMine;
    }

    public void setRead(boolean read){
        this.read = read;
    }

    public boolean isRead(){
        return read;
    }

    public void setMessageId(int messageId){
        this.messageId = messageId;
    }

    public int getMessageId(){
        return messageId;
    }

    public void setMessageType(int messageType){
        this.messageType = messageType;
    }

    public int getMessageType(){
        return messageType;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setFromUser(int fromUser){
        this.fromUser = fromUser;
    }

    public int getFromUser(){
        return fromUser;
    }

    public void setChatId(int chatId){
        this.chatId = chatId;
    }

    public int getChatId(){
        return chatId;
    }
}
