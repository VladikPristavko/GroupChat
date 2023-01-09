package main.dto;

public class DtoMessage {
    private String username;
    private String datetime;
    private String text;

    public DtoMessage(String username, String datetime, String text) {
        this.username = username;
        this.datetime = datetime;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
