package project;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class TradedMemeJoinMeme {

    private final SimpleIntegerProperty memeID;
    private final SimpleStringProperty phrase;
    private final SimpleStringProperty url;
    private final SimpleIntegerProperty upvotes;
    private final SimpleIntegerProperty downvotes;


    public TradedMemeJoinMeme(int memeID, String phrase, String url, int upvotes, int downvotes) {
        this.memeID = new SimpleIntegerProperty(memeID);
        this.phrase = new SimpleStringProperty(phrase);
        this.url = new SimpleStringProperty(url);
        this.upvotes = new SimpleIntegerProperty(upvotes);
        this.downvotes= new SimpleIntegerProperty(downvotes);

    }

    public int getMemeID() {
        return memeID.get();
    }

    public SimpleIntegerProperty MemeIDProperty() {
        return memeID;
    }

    public void setMemeID(int memeID) {
        this.memeID.set(memeID);
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

    public String getURL() {
        return url.get();
    }

    public SimpleStringProperty URLProperty() {
        return url;
    }

    public void setURL(String url) {
        this.url.set(url);
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
