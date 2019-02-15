package project;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Profile {


    private final SimpleStringProperty username;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty currency;


    public Profile(String username, String name, int currency) {
        this.username = new SimpleStringProperty(username);
        this.name = new SimpleStringProperty(name);
        this.currency = new SimpleIntegerProperty(currency);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCurrency() {
        return currency.get();
    }

    public SimpleIntegerProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency.set(currency);
    }

}
