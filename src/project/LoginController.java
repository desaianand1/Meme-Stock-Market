package project;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static final String memeStockMarketFXML = "mainLayout.fxml";
    private double x, y;
    private AnchorPane root = new AnchorPane();
    private Stage stage = new Stage();
    private Scene scene;
    private static final Random RANDOM = new SecureRandom();
    private static final Base64.Encoder enc = Base64.getEncoder();
    private static final Base64.Decoder dec = Base64.getDecoder();
    private DatabaseConnectionService dbService;
    @FXML
    private JFXTextField usernameText;
    @FXML
    private JFXPasswordField passwordText;
    @FXML
    private JFXButton registerButton;
    @FXML
    private JFXButton loginButton;
    @FXML
    private Label registerPrompt;
    @FXML
    private Label loginPrompt;
    @FXML
    private JFXTextField nameText;
    @FXML
    private ImageView nameIcon;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton closeButton;

    public LoginController(DatabaseConnectionService connectionService) {
        this.dbService = connectionService;
    }

    @FXML
    void onLoginClicked(ActionEvent event) {
        String username = usernameText.getText().trim();
        String password = passwordText.getText().trim();

        System.out.println("Login Clicked");
        login(username, password, event);
    }

    @FXML
    void onRegisterClicked(ActionEvent event) {
        System.out.println("Register Clicked");
        String username = usernameText.getText().trim();
        String password = passwordText.getText().trim();
        String name = nameText.getText().trim();

        if (username.equals("") || password.equals("")) {
            infoDialog("Username/Password field cannot be empty", "Empty Field", "Error", Alert.AlertType.ERROR);
            return;
        }
        if (name.equals("")) {
            name = "Leroy Jenkins";
        }
        if (!username.chars().allMatch(Character::isLetterOrDigit) || (!nameText.getText().trim().equals("") && !name.chars().allMatch(Character::isLetterOrDigit))) {
            infoDialog("Username/Name cannot have non-alphanumeric characters", "Invalid input", "Error", Alert.AlertType.ERROR);
            return;
        }
        register(username, password, name, event);
    }

    @FXML
    void onRegisterPromptClicked() {
        System.out.println("Register Prompt Clicked");
        loginButton.setVisible(false);
        registerPrompt.setVisible(false);
        loginPrompt.setVisible(true);
        registerButton.setVisible(true);
        nameText.setVisible(true);
        nameIcon.setVisible(true);
    }

    @FXML
    void onLoginPromptClicked() {
        System.out.println("Login Prompt Clicked");
        registerButton.setVisible(false);
        registerPrompt.setVisible(true);
        loginPrompt.setVisible(false);
        loginButton.setVisible(true);
        nameText.setVisible(false);
        nameIcon.setVisible(false);
    }

    boolean login(String username, String password, ActionEvent event) {
        PreparedStatement loginStatement;
        Connection connection = this.dbService.getConnection();
        String passHash = "";
        String passSalt = "";
        try {
            String stmt = "SELECT PasswordSalt,PasswordHash \nFROM Login\nWHERE ProfileUsername=?";
            loginStatement = connection.prepareStatement(stmt);
            loginStatement.setString(1, username);
            ResultSet rs = loginStatement.executeQuery();
            while (rs.next()) {
                passHash = rs.getString("PasswordHash");
                passSalt = rs.getString("PasswordSalt");
            }
            if (passHash.equals("") || passSalt.equals("")) {
                infoDialog("Login Failed", "Invalid Login", "Login Error", Alert.AlertType.ERROR);
                return false;
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: "+e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }
        String hash = hashPassword(getBytesFromString(passSalt), password);

        if (hash.equals(passHash)) {
            setupMemeStockMarketView(event, username);
            return true;
        } else {
            infoDialog("Login Failed", "Invalid Login", "Login Error", Alert.AlertType.ERROR);
            String salt = getStringFromBytes(getBytesFromString(passSalt));
            System.out.println("Username: " + username + "\n" + "Password: " + password + "\n" + "Salt: " + salt + "\n" + "Salt(DB): " + passSalt + "\n" + "Hash: " + hash + "\n" + "Hash(DB): " + passHash);
            return false;
        }
    }

    boolean register(String username, String password, String name, ActionEvent event) {
        byte[] salt = getNewSalt();
        String hash = hashPassword(salt, password);
        String saltString = getStringFromBytes(salt);
        try {
            CallableStatement cs = this.dbService.getConnection().prepareCall("{? = call Register(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            cs.setString(3, hash);
            cs.setString(4, saltString);
            cs.setString(5, name);
            cs.execute();
            int returnVal = cs.getInt(1);

            if (returnVal == 1) {
                infoDialog("Error: " + returnVal + ". Username already exists, try another one.", "Invalid Username", "Registration Error", Alert.AlertType.ERROR);
                return false;
            } else if (returnVal == 2) {
                infoDialog("Error: " + returnVal + ". Hash/Salt is invalid", "Invalid Hash/Salt", "Registration Error", Alert.AlertType.ERROR);
                return false;
            } else {
                infoDialog("Registered user: " + username + "\nName: " + name, "Registration Successful", "Success", Alert.AlertType.INFORMATION);
                setupMemeStockMarketView(event, username);
                return true;
            }

        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: "+e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;

        }

    }

    @FXML
    void onCloseView(ActionEvent event) {
        Platform.exit();
    }

    byte[] getNewSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    String getStringFromBytes(byte[] data) {
        return enc.encodeToString(data);
    }

    byte[] getBytesFromString(String data) {
        return dec.decode(data);
    }

    String hashPassword(byte[] salt, String password) {

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f;
        byte[] hash = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            infoDialog("An error occurred during password hashing. See stack trace.", "Hashing Error", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        return getStringFromBytes(hash);
    }

    private void setupMemeStockMarketView(Event event, String username) {
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();
        stage.close();

        this.stage.setTitle("Meme Stock Market");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/res/icons/icon.png")));

        try {
            // Load meme stock market view.
            FXMLLoader loader = new FXMLLoader();
            Controller controller = new Controller(dbService, username);
            loader.setController(controller);
            loader.setLocation(getClass().getResource(memeStockMarketFXML));
            AnchorPane memeStockMarketView = loader.load();
            Scene scene = new Scene(memeStockMarketView);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void infoDialog(String message, String header, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}