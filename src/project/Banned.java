package project;

import javafx.beans.property.SimpleStringProperty;

public class Banned {

    private final SimpleStringProperty username;
    private final SimpleStringProperty bannedDate;

    public Banned(String username, String bannedDate) {
        this.username = new SimpleStringProperty(username);
        this.bannedDate = new SimpleStringProperty(bannedDate);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getBannedDate() {
        return bannedDate.get();
    }

    public SimpleStringProperty bannedDateProperty() {
        return bannedDate;
    }

    public void setBannedDate(String bannedDate) {
        this.bannedDate.set(bannedDate);
    }
}
