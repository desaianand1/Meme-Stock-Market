package project;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Meme {


    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty phrase;
    private final SimpleStringProperty URL;
    private final SimpleIntegerProperty upvotes;
    private final SimpleIntegerProperty downvotes;
    private final SimpleBooleanProperty ageRestricted;
    private final SimpleStringProperty expDate;


    public Meme(int ID, String phrase, String URL, int upvotes, int downvotes, boolean ageRestricted, String expDate) {
        this.ID = new SimpleIntegerProperty(ID);
        this.phrase = new SimpleStringProperty(phrase);
        this.URL = new SimpleStringProperty(URL);
        this.upvotes = new SimpleIntegerProperty(upvotes);
        this.downvotes = new SimpleIntegerProperty(downvotes);
        this.ageRestricted = new SimpleBooleanProperty(ageRestricted);
        this.expDate = new SimpleStringProperty(expDate);

    }


    public String getExpDate() {
        return expDate.get();
    }

    public SimpleStringProperty expDateProperty() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate.set(expDate);
    }

    public boolean isAgeRestricted() {
        return ageRestricted.get();
    }

    public SimpleBooleanProperty ageRestrictedProperty() {
        return ageRestricted;
    }

    public void setAgeRestricted(boolean ageRestricted) {
        this.ageRestricted.set(ageRestricted);
    }

    public Integer getID() {
        return ID.get();
    }


    public void setID(Integer ID) {
        this.ID.set(ID);
    }

    public String getPhrase() {
        return phrase.get();
    }


    public void setPhrase(String phrase) {
        this.phrase.set(phrase);
    }

    public String getURL() {
        return URL.get();
    }


    public void setURL(String URL) {
        this.URL.set(URL);
    }

    public int getUpvotes() {
        return upvotes.get();
    }


    public void setUpvotes(int upvotes) {
        this.upvotes.set(upvotes);
    }

    public int getDownvotes() {
        return downvotes.get();
    }

    public void setDownvotes(int downvotes) {
        this.downvotes.set(downvotes);

    }
}
