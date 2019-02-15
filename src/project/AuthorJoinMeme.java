package project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AuthorJoinMeme {

    private SimpleIntegerProperty ID;
    private SimpleStringProperty phrase;
    private SimpleStringProperty madeDate;
    private SimpleStringProperty URL;
    private SimpleIntegerProperty upvotes;
    private SimpleIntegerProperty downvotes;

    AuthorJoinMeme(int ID, String phrase, String madeDate, String url, int upvotes, int downvotes) {
        this.ID = new SimpleIntegerProperty(ID);
        this.phrase = new SimpleStringProperty(phrase);
        this.madeDate = new SimpleStringProperty(madeDate);
        this.URL = new SimpleStringProperty(url);
        this.upvotes = new SimpleIntegerProperty(upvotes);
        this.downvotes = new SimpleIntegerProperty(downvotes);

    }


    public int getID() {
        return ID.get();
    }

    public SimpleIntegerProperty IDProperty() {
        return ID;
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public String getPhrase() {
        return phrase.get();
    }

    public SimpleStringProperty PhraseProperty() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase.set(phrase);
    }

    public String getMadeDate() {
        return madeDate.get();
    }

    public SimpleStringProperty MadeDateProperty() {
        return madeDate;
    }

    public void setMadeDate(String madeDate) {
        this.madeDate.set(madeDate);
    }

    public String getURL() {
        return URL.get();
    }

    public SimpleStringProperty URLProperty() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL.set(URL);
    }

    public int getUpvotes() {
        return upvotes.get();
    }

    public SimpleIntegerProperty UpvotesProperty() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes.set(upvotes);
    }

    public int getDownvotes() {
        return downvotes.get();
    }

    public SimpleIntegerProperty DownvotesProperty() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes.set(downvotes);
    }
}
