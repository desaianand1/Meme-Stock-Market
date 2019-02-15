package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class LoginMain extends Application {

    private static final int serverUsernameIndex = 0;
    private static final int serverPasswordIndex = 1;
    private static final int databaseNameIndex = 2;
    private static final int serverNameIndex = 3;
    private static final String loginFXML = "login.fxml";
    private double x, y;
    private static DatabaseConnectionService databaseConnectionService;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        LoginController loginController = new LoginController(databaseConnectionService);
        loader.setController(loginController);
        loader.setLocation(LoginMain.class.getResource(loginFXML));
        StackPane loginView = loader.load();

        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(loginView);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/res/icons/icon.png")));

        loginView.setOnMousePressed(event -> {
            x = stage.getX() - event.getScreenX();
            y = stage.getY() - event.getScreenY();
        });

        loginView.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + x);
            stage.setY(event.getScreenY() + y);
        });

        stage.show();
    }

    public static void main(String[] args) {
        ReadProperties readProperties = new ReadProperties();
        ArrayList<String> properties = readProperties.getPropertyValues();
        databaseConnectionService =
                new DatabaseConnectionService(properties.get(serverNameIndex), properties.get(databaseNameIndex));

        databaseConnectionService.connect(properties.get(serverUsernameIndex), properties.get(serverPasswordIndex));
        launch(args);
    }
}