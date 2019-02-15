package project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InvestsJoinMeme {
    private SimpleIntegerProperty num;
    private SimpleIntegerProperty TradedMemeID;
    private SimpleStringProperty URL;
    private SimpleIntegerProperty upvotes;
    private SimpleIntegerProperty downvotes;

    InvestsJoinMeme(int num, int tradedMemeID, String url, int upvotes, int downvotes) {
        this.num = new SimpleIntegerProperty(num);
        this.TradedMemeID = new SimpleIntegerProperty(tradedMemeID);
        this.URL = new SimpleStringProperty(url);
        this.upvotes = new SimpleIntegerProperty(upvotes);
        this.downvotes = new SimpleIntegerProperty(downvotes);

    }


    public int getnum() {
        return this.num.get();
    }

    public SimpleIntegerProperty numProperty() {
        return num;
    }

    public void setnum(int num) {
        this.num.set(num);
    }

    public Integer getTradedMemeID() {
        return TradedMemeID.get();
    }

    public SimpleIntegerProperty TradedMemeIDProperty() {
        return TradedMemeID;
    }

    public void setTradedMemeID(Integer TradedMemeID) {
        this.TradedMemeID.set(TradedMemeID);
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
