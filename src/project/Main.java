package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {

    private static final int serverUsernameIndex = 0;
    private static final int serverPasswordIndex = 1;
    private static final int databaseNameIndex = 2;
    private static final int serverNameIndex = 3;
    private static final String fxmlName="MemeStockMarketView.fxml";
    private static final String fxmlTest="test.fxml";
    private static DatabaseConnectionService databaseConnectionService;

    // private BorderPane rootLayout;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Meme Stock Market");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/res/icon.png")));
        initMemeStockMarketLayout();
    }


    /**
     * Shows the Meme Stock Market view
     */
    void initMemeStockMarketLayout() {
        try {
            // Load meme stock market view.
            FXMLLoader loader = new FXMLLoader();
            Controller controller = new Controller(databaseConnectionService,"desaiaj");
            loader.setController(controller);
            loader.setLocation(Main.class.getResource(fxmlTest));
            AnchorPane memeStockMarketView = loader.load();

            Scene scene = new Scene(memeStockMarketView);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


    /**
     * MAIN method. This launches app on init
     */
    public static void main(String[] args) {
        ReadProperties readProperties = new ReadProperties();
        ArrayList<String> properties = readProperties.getPropertyValues();
        databaseConnectionService =
                new DatabaseConnectionService(properties.get(serverNameIndex), properties.get(databaseNameIndex));

        databaseConnectionService.connect(properties.get(serverUsernameIndex), properties.get(serverPasswordIndex));
        launch(args);
    }
}
