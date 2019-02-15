package project;

import javafx.beans.property.SimpleIntegerProperty;

public class TradedMeme {

    private final SimpleIntegerProperty MemeID;
    private final SimpleIntegerProperty IsPopular;

    public TradedMeme(int memeID, int isPopular) {
        this.MemeID = new SimpleIntegerProperty(memeID);
        this.IsPopular = new SimpleIntegerProperty(isPopular);
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

    public int getIsPopular() {
        return IsPopular.get();
    }

    public SimpleIntegerProperty isPopularProperty() {
        return IsPopular;
    }

    public void setIsPopular(int isPopular) {
        this.IsPopular.set(isPopular);
    }
}
