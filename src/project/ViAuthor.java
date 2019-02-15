package project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ViAuthor {

    private final SimpleStringProperty Author;
    private final SimpleStringProperty Name;
    private final SimpleIntegerProperty PostNumber;


    public ViAuthor(String Author, String Name, int postNumber) {
        this.Author = new SimpleStringProperty(Author);
        this.Name = new SimpleStringProperty(Name);
        this.PostNumber = new SimpleIntegerProperty(postNumber);

    }

    public String getAuthor() {
        return Author.get();
    }

    public SimpleStringProperty authorProperty() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author.set(author);
    }

    public String getName() {
        return Name.get();
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public void setName(String name) {
        this.Name.set(name);
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
