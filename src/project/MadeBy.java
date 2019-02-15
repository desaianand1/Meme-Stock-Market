package project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MadeBy {
    private final SimpleIntegerProperty MemeID;
    private final SimpleStringProperty authorUsername;
    private final SimpleStringProperty madeDate;

    public MadeBy(int memeID, String authorUsername, String madeDate) {
        this.MemeID = new SimpleIntegerProperty(memeID);
        this.authorUsername = new SimpleStringProperty(authorUsername);
        this.madeDate = new SimpleStringProperty(madeDate);
    }

    public int getMemeID() {
        return MemeID.get();
    }

    public SimpleIntegerProperty memeIDProperty() {
        return MemeID;
    }

    public void setMemeID(int memeID) {
        this.MemeID.set(memeID);
    }

    public String getAuthorUsername() {
        return authorUsername.get();
    }

    public SimpleStringProperty authorUsernameProperty() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername.set(authorUsername);
    }

    public String getMadeDate() {
        return madeDate.get();
    }

    public SimpleStringProperty madeDateProperty() {
        return madeDate;
    }

    public void setMadeDate(String madeDate) {
        this.madeDate.set(madeDate);
    }
}
