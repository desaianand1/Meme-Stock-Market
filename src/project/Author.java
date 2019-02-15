package project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Author {

    private final SimpleStringProperty Username;
    private final SimpleIntegerProperty PostNumber;


    public Author(String Name, int postNumber) {
        this.Username = new SimpleStringProperty(Name);
        this.PostNumber = new SimpleIntegerProperty(postNumber);

    }

    public String getUsername() {
        return Username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username.set(username);
    }

    public int getPostNumber() {
        return PostNumber.get();
    }

    public SimpleIntegerProperty postNumberProperty() {
        return PostNumber;
    }

    public void setPostNumber(int postNumber) {
        this.PostNumber.set(postNumber);
    }
}
