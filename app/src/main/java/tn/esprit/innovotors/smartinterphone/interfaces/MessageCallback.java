package tn.esprit.innovotors.smartinterphone.interfaces;

import java.util.List;

import tn.esprit.innovotors.smartinterphone.models.Message;

public interface MessageCallback {

    void setMessages(List<Message> messages);

    void setError(String msg);

}
