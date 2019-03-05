package tn.esprit.innovotors.smartinterphone.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Message extends RealmObject {


    @PrimaryKey
    private String id;
    private String content;
    private Date displayedAt;
    private Date hiddenAt;
    private Date createdAt;
    private User user;
    private Device device;
    private String startDate;
    private String endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDisplayedAt() {
        return displayedAt;
    }

    public void setDisplayedAt(Date displayedAt) {
        this.displayedAt = displayedAt;
    }

    public Date getHiddenAt() {
        return hiddenAt;
    }

    public void setHiddenAt(Date hiddenAt) {
        this.hiddenAt = hiddenAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", displayedAt=" + displayedAt +
                ", hiddenAt=" + hiddenAt +
                ", user=" + user +
                ", device=" + device +
                '}';
    }
}
