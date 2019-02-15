package project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Template {

    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty description;
    private final SimpleIntegerProperty frequency;


    public Template(int ID, String description, int frequency) {
        this.ID = new SimpleIntegerProperty(ID);
        this.description = new SimpleStringProperty(description);
        this.frequency = new SimpleIntegerProperty(frequency);
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

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getFrequency() {
        return frequency.get();
    }

    public SimpleIntegerProperty frequencyProperty() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency.set(frequency);
    }


}
