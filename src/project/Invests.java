package project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Invests {
    private final SimpleStringProperty profileUsername;
    private final SimpleIntegerProperty tradedMemeID;
    private final SimpleIntegerProperty amount;
    private final SimpleIntegerProperty upvotes;
    private final SimpleIntegerProperty downvotes;



    public Invests(String profileUsername,int tradedMemeID,int amount,int upvotes,int downvotes) {
        this.profileUsername = new SimpleStringProperty(profileUsername);
      this.tradedMemeID = new SimpleIntegerProperty(tradedMemeID);
        this.amount = new SimpleIntegerProperty(amount);
        this.upvotes = new SimpleIntegerProperty(upvotes);
        this.downvotes= new SimpleIntegerProperty(downvotes);


    }

    public String getProfileUsername() {
        return profileUsername.get();
    }

    public SimpleStringProperty profileUsernameProperty() {
        return profileUsername;
    }

    public void setProfileUsername(String profileUsername) {
        this.profileUsername.set(profileUsername);
    }

    public int getTradedMemeID() {
        return tradedMemeID.get();
    }

    public SimpleIntegerProperty tradedMemeIDProperty() {
        return tradedMemeID;
    }

    public void setTradedMemeID(int tradedMemeID) {
        this.tradedMemeID.set(tradedMemeID);
    }

    public int getAmount() {
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public int getUpvotes() {
        return upvotes.get();
    }

    public SimpleIntegerProperty upvotesProperty() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes.set(upvotes);
    }

    public int getDownvotes() {
        return downvotes.get();
    }

    public SimpleIntegerProperty downvotesProperty() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes.set(downvotes);
    }
}
