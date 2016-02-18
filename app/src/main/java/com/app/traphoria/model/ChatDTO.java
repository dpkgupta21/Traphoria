package com.app.traphoria.model;


import java.util.List;

public class ChatDTO {
    private List<MessagesDTO> messageList;
    private String conversation_id;

    public List<MessagesDTO> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessagesDTO> messageList) {
        this.messageList = messageList;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }
}
