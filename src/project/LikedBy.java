package project;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LikedBy {
    private final SimpleIntegerProperty MemeID;
    private final SimpleStringProperty ProfileUsername;
    private final SimpleStringProperty LikeDislike;
    private final SimpleStringProperty MadeDate;


    public LikedBy(int MemeID, String ProfileUsername, String LikeDislike, String MadeDate) {
        this.MemeID = new SimpleIntegerProperty(MemeID);
        this.ProfileUsername = new SimpleStringProperty(ProfileUsername);
        this.LikeDislike = new SimpleStringProperty(LikeDislike);
        this.MadeDate = new SimpleStringProperty(MadeDate);

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

    public String getProfileUsername() {
        return ProfileUsername.get();
    }

    public SimpleStringProperty profileUsernameProperty() {
        return ProfileUsername;
    }

    public void setProfileUsername(String profileUsername) {
        this.ProfileUsername.set(profileUsername);
    }

    public String getLikeDislike() {
        return LikeDislike.get();
    }

    public SimpleStringProperty likeDislikeProperty() {
        return LikeDislike;
    }

    public void setLikeDislike(String likeDislike) {
        this.LikeDislike.set(likeDislike);
    }

    public String getMadeDate() {
        return MadeDate.get();
    }

    public SimpleStringProperty madeDateProperty() {
        return MadeDate;
    }

    public void setMadeDate(String madeDate) {
        this.MadeDate.set(madeDate);
    }
}
