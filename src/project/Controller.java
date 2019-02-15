package project;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private DatabaseConnectionService connection;
    private String username;
    private static final String loginFXML = "login.fxml";


    public Controller(DatabaseConnectionService databaseConnectionService, String username) {
        this.connection = databaseConnectionService;
        this.username = username;
    }

    @FXML
    private Label currencyText;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton dropTableButton;
    @FXML
    private JFXButton logOutButton;
    @FXML
    private JFXComboBox selectTable = new JFXComboBox();
    @FXML
    private JFXComboBox selectColumn = new JFXComboBox();
    @FXML
    private JFXTextField selectValueText;
    @FXML
    private JFXButton searchButton;
    @FXML
    private JFXComboBox callProcDropDown = new JFXComboBox();
    @FXML
    private TableView msm_table = new TableView();
    @FXML
    private TextArea consoleTextArea = new TextArea();
    @FXML
    private ImageView memeImage;
    @FXML
    private JFXButton importButton;
    @FXML
    private Tab authorTab;
    @FXML
    private JFXButton callProcButton;
    @FXML
    private JFXTextField procText;
    @FXML
    private JFXToggleButton likeDislikeToggle;
    @FXML
    private Label usernameText;
    @FXML
    private Label tableText;
    @FXML
    private JFXTextField memePhraseText;
    @FXML
    private JFXTextField memeUrlText;
    @FXML
    private JFXCheckBox ageRestricted;
    @FXML
    private JFXTextArea templateDescriptionText;
    @FXML
    private JFXButton browserButton;
    @FXML
    private JFXButton checkImageButton;
    @FXML
    private JFXButton createTableButton;
    @FXML
    private JFXButton creditButton;
    @FXML
    private Tab addMemeTab;
    @FXML
    private Tab investTab;
    @FXML
    private Tab msmTab;
    @FXML
    private WebView webView;
    @FXML
    private JFXTextField templateTagText;
    @FXML
    private ImageView addMemeImage;
    @FXML
    private Label memeUsernameText;
    @FXML
    private JFXButton closeBrowserButton;
    @FXML
    private JFXButton addMemeButton;
    @FXML
    private ImageView browserPlaceholder;
    @FXML
    private JFXButton investButton;
    @FXML
    private JFXTextField investAmountText;
    @FXML
    private JFXTextField reapText;
    @FXML
    private JFXTextField tradedMemeText;
    @FXML
    private JFXButton tradedMemeButton;
    @FXML
    private Label investUsernameText;
    @FXML
    private JFXButton reapButton;
    @FXML
    private TableView<?> investTable;
    @FXML
    private JFXTextField investIdText;


    private static final String imgurURL = "https://imgur.com/upload";
    private PrintStream printStream;
    private HashMap<String, ObservableList<String>> columnMap;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        assert msm_table != null : "fx:id=\"msm_table\" was not injected: check your FXML file 'mainLayout.fxml'.";
        printStream = new PrintStream(new Console(consoleTextArea));
        System.setOut(printStream);
//        System.setErr(printStream);
        populateDropDownList();
        populateColumnMap();
        populateTableDropDownList();
        usernameText.setText(username);
        memeUsernameText.setText(username);
        investUsernameText.setText(username);

        //setup tab pane with icons
        setupTabPane();
        //setup Meme table on startup
        tableText.setText("Meme");
        likeDislikeToggle.setVisible(false);
        procText.setPromptText("Value");

        currencyText.setText(getCurrencyValue());
        try {
            buildAllMemes();
            //setup table image listener/loader
            tableUrlListener();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTabPane() {

        ImageView icon_1 = new ImageView(new Image(getClass().getResourceAsStream("/res/icons/pepe_icon.png")));
        icon_1.setFitWidth(18);
        icon_1.setFitHeight(18);
        msmTab.setGraphic(icon_1);

        ImageView icon_2 = new ImageView(new Image(getClass().getResourceAsStream("/res/icons/add_icon_round.png")));
        icon_2.setFitWidth(18);
        icon_2.setFitHeight(18);
        addMemeTab.setGraphic(icon_2);

        ImageView icon_3 = new ImageView(new Image(getClass().getResourceAsStream("/res/icons/coin_icon_white.png")));
        icon_3.setFitWidth(18);
        icon_3.setFitHeight(18);
        investTab.setGraphic(icon_3);
    }

    @FXML
    void openBrowser(ActionEvent event) {
        WebEngine webEngine = webView.getEngine();
        webEngine.load(imgurURL);
        browserPlaceholder.setVisible(false);
        webView.setVisible(true);

    }

    @FXML
    void closeBrowser(ActionEvent event) {
        webView.getEngine().load(null);
        webView.setVisible(false);
        browserPlaceholder.setVisible(true);
    }

    private void tableUrlListener() {

        msm_table.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (msm_table.getSelectionModel().getSelectedItems().size() > 0 &&
                        msm_table.getSelectionModel().getSelectedItems().get(0) instanceof Meme) {

                    ObservableList<Meme> row = this.msm_table.getSelectionModel().getSelectedItems();

                    System.out.println("url: " + row.get(0).getURL());
                    loadMemeImage(row.get(0).getURL());

                } else if (msm_table.getSelectionModel().getSelectedItems().size() > 0 &&
                        msm_table.getSelectionModel().getSelectedItems().get(0) instanceof TradedMemeJoinMeme) {

                    ObservableList<TradedMemeJoinMeme> row = this.msm_table.getSelectionModel().getSelectedItems();

                    System.out.println("url: " + row.get(0).getURL());
                    loadMemeImage(row.get(0).getURL());

                } else if (msm_table.getSelectionModel().getSelectedItems().size() > 0 &&
                        msm_table.getSelectionModel().getSelectedItems().get(0) instanceof InvestsJoinMeme) {

                    ObservableList<InvestsJoinMeme> row = this.msm_table.getSelectionModel().getSelectedItems();

                    System.out.println("url: " + row.get(0).getURL());
                    loadMemeImage(row.get(0).getURL());
                }
            } catch (Exception e) {
                infoDialog(e.getMessage(), "URL Listener Error", "Error", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        });
    }

    private void loadMemeImage(String url) {
        Image image = new Image(url);
        memeImage.setImage(image);
    }

    private void populateDropDownList() {
        ObservableList<String> procedureList =
                FXCollections.observableArrayList(
                        "GetAllTradedMemes",
                        "GetMemesByAuthor",
                        "GetMemesByLikeNumber",
                        "GetMemesByDislikeNumber",
                        "GetUndergroundMemes",
                        "GetAllMemes",
                        "GetNewMemes",
                        "ViewAuthor",
                        "LikeMeme",
                        "GetMemesByInvestorCount",
                        "GetInvestmentsForProfile"
                );
        this.callProcDropDown.setItems(procedureList);
    }

    private void populateTableDropDownList() {
        ObservableList<String> tableList =
                FXCollections.observableArrayList(
                        "Meme",
                        "Profile",
                        "Template",
                        "Invests",
                        "TradedMeme",
                        "Author",
                        "MadeBy",
                        "LikedBy",
                        "Banned"
                );
        this.selectTable.setItems(tableList);
    }

    private void populateColumnMap() {
        this.columnMap = new HashMap<>();
        columnMap.put("Meme", FXCollections.observableArrayList("ID", "Phrase", "AgeRestricted", "URL", "ExpDate", "Upvotes", "Downvotes"));
        columnMap.put("Profile", FXCollections.observableArrayList("Username", "Name", "Currency"));
        columnMap.put("Template", FXCollections.observableArrayList("ID", "Description", "Frequency"));
        columnMap.put("Invests", FXCollections.observableArrayList("ProfileUsername", "TradedMemeID", "Amount", "Upvotes", "Downvotes"));
        columnMap.put("TradedMeme", FXCollections.observableArrayList(" MemeID", "isPopular"));
        columnMap.put("Author", FXCollections.observableArrayList("Username", "Postnumber"));
        columnMap.put("MadeBy", FXCollections.observableArrayList("MemeID", "AuthorName", "MadeDate"));
        columnMap.put("LikedBy", FXCollections.observableArrayList("MemeID", "ProfileUsername", "[LikeDislike]", "MadeDate"));
        columnMap.put("Banned", FXCollections.observableArrayList("Username", "BannedDate"));

    }

    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char) b));
        }
    }

    @FXML
    void onLikeDislikeToggle(ActionEvent event) {
        if (likeDislikeToggle.getText().equals("Like")) {
            likeDislikeToggle.setText("Dislike");
        } else {
            likeDislikeToggle.setText("Like");
        }
    }

    @FXML
    void onProcSelected(ActionEvent event) {

        String selectedItem = callProcDropDown.getValue().toString();

        switch (selectedItem) {
            case "GetAllTradedMemes":
                procText.setPromptText("Value");
                procText.setDisable(true);
                likeDislikeToggle.setVisible(false);
                break;
            case "GetMemesByAuthor":
                procText.setPromptText("Author Name");
                procText.setDisable(false);
                likeDislikeToggle.setVisible(false);
                break;
            case "GetMemesByLikeNumber":
                procText.setPromptText("No. of Likes");
                procText.setDisable(false);
                likeDislikeToggle.setVisible(false);
                break;
            case "GetMemesByDislikeNumber":
                procText.setPromptText("No. of Dislikes");
                procText.setDisable(false);
                likeDislikeToggle.setVisible(false);
                break;
            case "GetUndergroundMemes":
                procText.setPromptText("Value");
                procText.setDisable(true);
                likeDislikeToggle.setVisible(false);
                break;
            case "GetAllMemes":
                procText.setPromptText("Value");
                procText.setDisable(true);
                likeDislikeToggle.setVisible(false);
                break;
            case "GetNewMemes":
                procText.setPromptText("Value");
                procText.setDisable(true);
                likeDislikeToggle.setVisible(false);
                break;
            case "ViewAuthor":
                procText.setPromptText("Author Name");
                procText.setDisable(false);
                likeDislikeToggle.setVisible(false);
                break;
            case "LikeMeme":
                procText.setPromptText("Meme ID");
                procText.setDisable(false);
                likeDislikeToggle.setVisible(true);
                break;
            case "GetMemesByInvestorCount":
                procText.setPromptText("No. of Investors");
                procText.setDisable(false);
                likeDislikeToggle.setVisible(false);
                break;
            case "GetInvestmentsForProfile":
                procText.setPromptText("Username");
                procText.setDisable(false);
                likeDislikeToggle.setVisible(false);
                break;
            default:
                procText.setPromptText("Value");
                procText.setDisable(true);
                likeDislikeToggle.setVisible(false);
                break;
        }
    }

    @FXML
    void onCallProcButtonClicked(ActionEvent event) {


        if (callProcDropDown.getValue() != null && !callProcDropDown.getValue().toString().equals("") && !procText.getText().equals("") || procText.isDisable()) {
            msm_table.getColumns().clear();
        } else {
            System.out.println("Dropdown Selected: " + callProcDropDown.getValue() + "\nText: " + procText.getText() + "\n is Text Disable: " + procText.isDisable());
            infoDialog(null, "Invalid Input", "Error", Alert.AlertType.ERROR);
            return;
        }
        switch (this.callProcDropDown.getValue().toString()) {
            case "GetAllTradedMemes":
                try {
                    tableText.setText("Traded Meme");
                    buildTradedMemes();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetAllTradedMemes");
                break;
            case "GetMemesByAuthor":
                try {
                    tableText.setText("Meme");
                    String author = procText.getText().trim();
                    if (author.chars().allMatch(Character::isLetter)) {
                        buildMemesByAuthor(author);
                    } else {
                        infoDialog("Author: " + author + " is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetMemesByAuthor");
                break;
            case "GetMemesByLikeNumber":
                try {
                    tableText.setText("Meme");
                    String likeNumber = procText.getText().trim();
                    if (likeNumber.chars().allMatch(Character::isDigit) && Integer.parseInt(likeNumber) >= 0) {
                        buildMemesByLikeNumber(Integer.parseInt(likeNumber));
                    } else {
                        infoDialog("Like Value: " + likeNumber + " is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetMemesByLikeNumber");
                break;
            case "GetMemesByDislikeNumber":
                try {
                    tableText.setText("Meme");
                    String dislikeNumber = procText.getText().trim();
                    if (dislikeNumber.chars().allMatch(Character::isDigit) && Integer.parseInt(dislikeNumber) >= 0) {
                        buildMemesByDislikeNumber(Integer.parseInt(dislikeNumber));

                    } else {
                        infoDialog("Like Value: " + dislikeNumber + " is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetMemesByDislikeNumber");
                break;
            case "GetUndergroundMemes":
                try {
                    tableText.setText("Underground Meme");
                    buildUndergroundMemes();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetUndergroundMemes");
                break;
            case "GetAllMemes":
                try {
                    tableText.setText("Meme");
                    buildAllMemes();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetAllMemes");
                break;
            case "GetNewMemes":
                try {
                    tableText.setText("Meme");
                    buildNewMemes();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.println("GetNewMemes");
                break;
            case "ViewAuthor":
                try {
                    tableText.setText("Author");
                    String author = procText.getText().trim();
                    if (author.chars().allMatch(Character::isLetter)) {
                        buildViewAuthor(author);
                    } else {
                        infoDialog("Author: " + author + " is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("ViewAuthor");
                break;
            case "LikeMeme":
                try {
                    tableText.setText("Meme");
                    boolean likeDislikeValue = likeDislikeToggle.getText().equals("Like");
                    String memeID = procText.getText().trim();

                    if (memeID.chars().allMatch(Character::isDigit) && Integer.parseInt(memeID) >= 0) {
                        System.out.println("username: " + username);
                        buildLikeMeme(username, likeDislikeValue, Integer.parseInt(memeID));
                    } else {
                        infoDialog("Meme ID: " + memeID + " is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("LikeMeme");
                break;
            case "GetMemesByInvestorCount":
                try {
                    tableText.setText("Meme");
                    String numInvestments = procText.getText().trim();
                    if (numInvestments.chars().allMatch(Character::isDigit) && Integer.parseInt(numInvestments) >= 0) {
                        buildInvestmentsByCount(Integer.parseInt(numInvestments));
                    } else {
                        infoDialog("Investment value: " + numInvestments + " is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetMemesByInvestorCount");
                break;
            case "GetInvestmentsForProfile":
                try {
                    tableText.setText("Invests");
                    String username = procText.getText().trim();
                    if (username.chars().allMatch(Character::isLetter)) {
                        buildInvestmentsForProfile(username);
                    } else {
                        infoDialog("Username value: " + username + " is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("GetInvestmentsForProfile");
                break;
            default:                                    //I didn't even try to add GetMemesByInvestorCount
                System.out.println("Nothing selected");
        }
    }

    @FXML
    void onImportButtonClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Open File");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Data Files", "*.csv,*.dat,*.log,*.db,*.mdb,*.sql,*.xml,*.tar"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Excel", "*.xls"));
        File file = chooser.showOpenDialog(stage);
        if (file != null && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MemeLoader loader = new MemeLoader(this.connection, file);
        loader.loadMemes();
    }


    @FXML
    void onDropButtonClicked(ActionEvent event) {
        boolean confirmed = infoDialog("Are you sure you want to drop all tables?", "Drop Tables?", "Confirmation", Alert.AlertType.CONFIRMATION);
        if (confirmed) {
            // drop tables
        }
    }

    @FXML
    void onCreateTableClicked(ActionEvent event) {
        boolean confirmed = infoDialog("Are you sure you want to re-create all tables?", "Re-create Tables?", "Confirmation", Alert.AlertType.CONFIRMATION);
        if (confirmed) {
            // re-create tables
        }
    }

    @FXML
    void onCreditsClicked(ActionEvent event) {
        infoDialog("Anand Desai, Dennis Colomes, Grant Hartman", "Credits:", "Credits", Alert.AlertType.INFORMATION);
    }

    @FXML
    void onCheckImageClicked(ActionEvent event) {
        if (!memeUrlText.getText().equals("")) {
            try {
                Image image = new Image(memeUrlText.getText());
                addMemeImage.setImage(image);
            } catch (IllegalArgumentException e) {
                infoDialog("Make sure the url is valid", "Image URL Invalid", "Error", Alert.AlertType.ERROR);
                //e.printStackTrace();
            }
        }
    }

    @FXML
    void onCloseView(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onLogOutClicked(ActionEvent event) {
        System.out.println("Log Out Clicked");
        boolean confirmed = infoDialog("Are you sure you want to Log Out?", "Log Out?", "Confirmation", Alert.AlertType.CONFIRMATION);
        if (confirmed) {
            logOut(event);
        }
    }

    private void logOut(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        stage.setTitle("Login");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/res/icons/icon.png")));

        try {
            // Load Login View.
            FXMLLoader loader = new FXMLLoader();
            LoginController loginController = new LoginController(connection);
            loader.setController(loginController);
            loader.setLocation(getClass().getResource(loginFXML));
            StackPane loginView = loader.load();
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSearchClicked(ActionEvent event) {

        PreparedStatement statement;

        try {
            StringBuilder query = new StringBuilder();
            String whereClause = "";

            if (this.selectTable.getValue() == null) {
                infoDialog("Select a Table!", "No Table Selected", "ERROR", Alert.AlertType.ERROR);
                return;
            }

            String selectedTable = this.selectTable.getValue().toString();
            String inputValue = this.selectValueText.getText();


            if (this.selectColumn.getValue() == null && inputValue.equals("")) {
                whereClause = "";

            }
            if (this.selectColumn.getValue() == null && !inputValue.equals("")) {
                infoDialog("Select a Column!", "No Column Selected", "ERROR", Alert.AlertType.ERROR);
                return;
            }
            if (this.selectColumn.getValue() != null && inputValue.equals("")) {
                whereClause = "";
            }
            if (this.selectColumn.getValue() != null && !inputValue.equals("")) {
                String selectedColumn = this.selectColumn.getValue().toString();
                whereClause = String.format(" WHERE %s = ?", selectedColumn);
            }

            query.append(String.format("SELECT * FROM %s", selectedTable));
            query.append(whereClause);

            String finalQuery = query.toString().trim();
            System.out.println("Query: " + finalQuery);
            statement = connection.getConnection().prepareStatement(finalQuery);

            if (this.selectColumn != null && !inputValue.equals("")) {
                statement.setString(1, inputValue);
            }
            ResultSet rs = statement.executeQuery();

            msm_table.getColumns().clear();
            msm_table.getItems().clear();
            tableText.setText(selectedTable);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                TableColumn column = new TableColumn<>();
                switch (rs.getMetaData().getColumnName(i + 1)) {

                    case "ProfileUsername":
                        column.setText("Username");
                        break;
                    case "TradedMemeID":
                        column.setText("Meme ID");
                        break;
                    case "MemeID":
                        column.setText("Meme ID");
                        break;
                    case "AuthorName":
                        column.setText("Author");
                        break;
                    case "MadeDate":
                        column.setText("Date");
                        break;
                    case "PostNumber":
                        column.setText("Memes Posted");
                        break;
                    case "BannedDate":
                        column.setText("Banned Date");
                        break;
                    case "LikeDislike":
                        column.setText("Like/Dislike");
                    default:
                        column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                        break;
                }
                column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
                msm_table.getColumns().add(column);
            }

            ObservableList dbData;

            switch (selectedTable) {
                case "Meme":
                    dbData = FXCollections.observableArrayList(memeArrayList(rs));
                    break;
                case "Profile":
                    dbData = FXCollections.observableArrayList(profileArrayList(rs));
                    break;
                case "Template":
                    dbData = FXCollections.observableArrayList(templateArrayList(rs));
                    break;
                case "Invests":
                    dbData = FXCollections.observableArrayList(investsArrayList(rs));
                    break;
                case "TradedMeme":
                    dbData = FXCollections.observableArrayList(tradedMemeArrayList(rs));
                    break;
                case "Author":
                    dbData = FXCollections.observableArrayList(authorArrayList(rs));
                    break;
                case "MadeBy":
                    dbData = FXCollections.observableArrayList(madeByArrayList(rs));
                    break;
                case "LikedBy":
                    dbData = FXCollections.observableArrayList(likedbyArrayList(rs));
                    break;
                case "Banned":
                    dbData = FXCollections.observableArrayList(bannedArrayList(rs));
                    break;
                default:
                    dbData = null;
                    infoDialog("No such table!", "Uh Oh", "Error", Alert.AlertType.ERROR);
                    break;
            }
            msm_table.setItems(dbData);
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            // e.printStackTrace();
            return;
        }
    }

    @FXML
    void onAddMemeClicked(ActionEvent event) {

        String phrase = memePhraseText.getText().trim();
//        if (!phrase.chars().allMatch(Character::isSpaceChar) && !phrase.chars().allMatch(Character::isLetter) && !phrase.chars().allMatch(Character::isDigit)) {
//            infoDialog("Phrase is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
//        }
        boolean age = ageRestricted.isSelected();

        String desc = templateDescriptionText.getText().trim();
//        if (!desc.chars().allMatch(Character::isLetter) && !desc.chars().allMatch(Character::isDigit) && !desc.chars().allMatch(Character::isSpaceChar)) {
//            infoDialog("Description is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
//        }
        String tag = templateTagText.getText().trim();
//        if (!tag.chars().allMatch(Character::isLetter) && !tag.chars().allMatch(Character::isSpaceChar) && !tag.chars().allMatch(Character::isDigit)) {
//            infoDialog("Tag is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
//        }
        String url = memeUrlText.getText().trim();

        try {
            buildAddMemes(phrase, age, desc, tag, url);
            infoDialog("Meme Added", "Success", "Success", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Error " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            return;
        }


    }

    @FXML
    void onSelectTable(ActionEvent event) {
        String selectedTable = this.selectTable.getValue().toString();
        setupColumnDropDown(selectedTable);
    }

    private void setupColumnDropDown(String key) {
        System.out.println("Table selected:" + key);
        ObservableList<String> columnList = this.columnMap.get(key);
        this.selectColumn.setItems(columnList);
    }


    private void buildLikeMeme(String profileName, boolean liked, int memeID) throws SQLException {
        LikeMeme likeMeme = new LikeMeme(this.connection);

        ResultSet rs = likeMeme.likeMeme(profileName, liked, memeID);
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(likedbyArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "MemeID":
                    column.setText("MemeID");
                    break;
                case "ProfileUsername":
                    column.setText("ProfileUsername");
                    break;
                case "LikeDislike":
                    column.setText("Like/Dislike");
                    break;
                case "MadeDate":
                    column.setText("MadeDate");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);
    }

    private void buildViewAuthor(String authorName) throws SQLException {
        ViewAuthor author = new ViewAuthor(this.connection);
        ResultSet rs = author.getAuthor(authorName);
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(viewAuthorArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "Author":
                    column.setText("Author");
                    break;
                case "Name":
                    column.setText("Name");
                    break;
                case "PostNumber":
                    column.setText("PostNumber");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);
    }

    void buildNewMemes() throws SQLException {
        GetNewMemes NMemes = new GetNewMemes(this.connection);
        ResultSet rs = NMemes.getNewMemes();
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(authorJoinMemeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "ID":
                    column.setText("ID");
                    break;
                case "Phrase":
                    column.setText("Phrase");
                    break;
                case "URL":
                    column.setText("URL");
                    break;
                case "Upvotes":
                    column.setText("Upvotes");
                    break;
                case "Downvotes":
                    column.setText("Downvotes");
                    break;
                case "MadeDate":
                    column.setText("MadeDate");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);
    }

    void buildUndergroundMemes() throws SQLException {
        GetUndergroundMemes GBLike = new GetUndergroundMemes(this.connection);
        ResultSet rs = GBLike.getUndergroundMemes();
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(investsJoinMemeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "num":
                    column.setText("InvestorCount");
                    break;
                case "TradedMemeID":
                    column.setText("ID");
                    break;
                case "URL":
                    column.setText("URL");
                    break;
                case "Upvotes":
                    column.setText("Upvotes");
                    break;
                case "Downvotes":
                    column.setText("Downvotes");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);
    }

    //method for getting memes by a given author
    void buildMemesByAuthor(String authorName) throws SQLException {
        GetMemesByAuthor GBLike = new GetMemesByAuthor(this.connection);
        ResultSet rs = GBLike.getMemesByAuthor(authorName); //hard coded author get
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(authorJoinMemeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "ID":
                    column.setText("ID");
                    break;
                case "Phrase":
                    column.setText("Phrase");
                    break;
                case "MadeDate":
                    column.setText("MadeDate");
                    break;
                case "URL":
                    column.setText("URL");
                    break;
                case "Upvotes":
                    column.setText("Upvotes");
                    break;
                case "Downvotes":
                    column.setText("Downvotes");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);

    }

    //method for getting memes by a given like number
    void buildMemesByDislikeNumber(int dislikeNumber) throws SQLException {
        GetMemesByDislikeNumber GBLike = new GetMemesByDislikeNumber(this.connection);
        ResultSet rs = GBLike.getMemesByDislikeNumber(dislikeNumber); //hard coded like get
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(memeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "ID":
                    column.setText("ID");
                    break;
                case "Phrase":
                    column.setText("Phrase");
                    break;
                case "URL":
                    column.setText("URL");
                    break;
                case "Upvotes":
                    column.setText("Upvotes");
                    break;
                case "Downvotes":
                    column.setText("Downvotes");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);

    }

    //gets all memes, ordered by newest
    void buildAllMemes() throws SQLException {
        GetAllMemes nMemes = new GetAllMemes(this.connection);
        ResultSet rs = nMemes.getMemes();
        ObservableList dbData = null;

        dbData = FXCollections.observableArrayList(memeArrayList(rs));


        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "ID":
                    column.setText("ID");
                    break;
                case "Phrase":
                    column.setText("Phrase");
                    break;
                case "URL":
                    column.setText("URL");
                    break;
                case "Upvotes":
                    column.setText("Upvotes");
                    break;
                case "Downvotes":
                    column.setText("Downvotes");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);

    }

    //method for getting all traded memes
    void buildTradedMemes() throws SQLException {
        GetAllTradedMemes tMemes = new GetAllTradedMemes(this.connection);
        ResultSet rs = tMemes.getTradedMemes();
        ObservableList dbData = null;

        dbData = FXCollections.observableArrayList(tradedMemeJoinMemeArrayList(rs));


        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "MemeID":
                    column.setText("ID");
                    break;
                case "Phrase":
                    column.setText("Phrase");
                    break;
                case "URL":
                    column.setText("URL");
                    break;
                case "Upvotes":
                    column.setText("Upvotes");
                    break;
                case "Downvotes":
                    column.setText("Downvotes");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);

    }

    //method for getting memes by a given like number
    void buildMemesByLikeNumber(int likeNumber) throws SQLException {
        GetMemesByLikeNumber GBLike = new GetMemesByLikeNumber(this.connection);
        ResultSet rs = GBLike.getMemesByLikeNumber(likeNumber);
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(memeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "ID":
                    column.setText("ID");
                    break;
                case "Phrase":
                    column.setText("Phrase");
                    break;
                case "URL":
                    column.setText("URL");
                    break;
                case "Upvotes":
                    column.setText("Upvotes");
                    break;
                case "Downvotes":
                    column.setText("Downvotes");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);

    }

    private void buildInvestmentsByCount(int numInvestments) throws SQLException {
        GetMemesByInvestorCount GBInvCount = new GetMemesByInvestorCount(this.connection);
        ResultSet rs = GBInvCount.getMemesByInvestors(numInvestments);
        ObservableList dbData = null;
        dbData = FXCollections.observableArrayList(investsJoinMemeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "num":
                    column.setText("Total Invested");
                    break;

                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);

    }

    private void buildInvestmentsForProfile(String username) throws SQLException {
        GetInvestmentsForProfile investmentsForProfile = new GetInvestmentsForProfile(this.connection);
        ResultSet rs = investmentsForProfile.getInvestments(username);
        ObservableList dbData = null;
        try {
            dbData = FXCollections.observableArrayList(investsArrayList(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "TradedMemeID":
                    column.setText("Meme ID");
                    break;
                case "ProfileUsername":
                    column.setText("Username");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);

    }

    private void buildAddInvestment(String username, int memeId, int amount) throws SQLException {
        AddInvestment addInvestment = new AddInvestment(this.connection);
        ResultSet rs = addInvestment.addInvestment(username, memeId, amount);
        ObservableList dbData = null;
        try {
            dbData = FXCollections.observableArrayList(investsArrayList(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "TradedMemeID":
                    column.setText("Meme ID");
                    break;
                case "ProfileUsername":
                    column.setText("Username");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            investTable.getColumns().add(column);
        }

        //Filling up tableView with data
        investTable.setItems(dbData);

    }

    private void buildReapInvestment(String username, int memeId) throws SQLException {
        ReapInvestment reapInvestment = new ReapInvestment(this.connection);
        ResultSet rs = reapInvestment.reapInvestment(username, memeId);
        ObservableList dbData = null;
        try {
            dbData = FXCollections.observableArrayList(investsArrayList(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "TradedMemeID":
                    column.setText("Meme ID");
                    break;
                case "ProfileUsername":
                    column.setText("Username");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            investTable.getColumns().add(column);
        }

        //Filling up tableView with data
        investTable.setItems(dbData);

    }

    private void buildAddTradedMemes(String username, int memeId) throws SQLException {

        AddTradedMeme addTradedMeme = new AddTradedMeme(this.connection);
        ResultSet rs = addTradedMeme.addTradedMeme(username, memeId);
        ObservableList dbData = null;

        dbData = FXCollections.observableArrayList(tradedMemeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "TradedMemeID":
                    column.setText("Meme ID");

                    break;
                case "ProfileUsername":
                    column.setText("Username");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            investTable.getColumns().add(column);
        }

        //Filling up tableView with data
        investTable.setItems(dbData);

    }

    private void buildAddMemes(String phrase, boolean age, String desc, String tag, String url) throws SQLException {
        AddMeme addMeme = new AddMeme(this.connection);
        ResultSet rs = addMeme.addMeme(phrase, age, url, username);

        ObservableList dbData = null;

        dbData = FXCollections.observableArrayList(memeArrayList(rs));


        //Giving readable names to columns
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            TableColumn column = new TableColumn<>();
            switch (rs.getMetaData().getColumnName(i + 1)) {
                case "TradedMemeID":
                    column.setText("Meme ID");
                    break;
                case "ProfileUsername":
                    column.setText("Username");
                    break;
                default:
                    column.setText(rs.getMetaData().getColumnName(i + 1)); //if column name in SQL Database is not found, then TableView column receive SQL Database current column name (not readable)
                    break;
            }
            column.setCellValueFactory(new PropertyValueFactory<>(rs.getMetaData().getColumnName(i + 1))); //Setting cell property value to correct variable from Person class.
            msm_table.getColumns().add(column);
        }

        //Filling up tableView with data
        msm_table.setItems(dbData);
    }

    //extracting data from ResulSet to meme table ArrayList
    private ArrayList memeArrayList(ResultSet rs) {
        ArrayList<Meme> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                Meme meme = new Meme(
                        rs.getInt("ID"),
                        rs.getString("Phrase"),
                        rs.getString("URL"),
                        rs.getInt("Upvotes"),
                        rs.getInt("Downvotes"),
                        rs.getBoolean("AgeRestricted"),
                        rs.getDate("ExpDate").toString());
                data.add(meme);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    //extracting data from ResultSet to TradedMemeJoinMeme table arraylist
    private ArrayList tradedMemeJoinMemeArrayList(ResultSet rs) {
        ArrayList<TradedMemeJoinMeme> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                TradedMemeJoinMeme tradeMeme = new TradedMemeJoinMeme(
                        rs.getInt("MemeID"),
                        rs.getString("Phrase"),
                        rs.getString("URL"),
                        rs.getInt("Upvotes"),
                        rs.getInt("Downvotes"));
                data.add(tradeMeme);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    //extracting data from ResultSet to authorJoinMeme table arraylist
    private ArrayList authorJoinMemeArrayList(ResultSet rs) {
        ArrayList<AuthorJoinMeme> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                AuthorJoinMeme authorMeme = new AuthorJoinMeme(
                        rs.getInt("ID"),
                        rs.getString("Phrase"),
                        rs.getString("MadeDate"),
                        rs.getString("URL"),
                        rs.getInt("Upvotes"),
                        rs.getInt("Downvotes"));
                data.add(authorMeme);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    //extracting data from ResultSet to investsJoinMeme table arraylist
    private ArrayList investsJoinMemeArrayList(ResultSet rs) {
        ArrayList<InvestsJoinMeme> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                InvestsJoinMeme investedMeme = new InvestsJoinMeme(
                        rs.getInt("num"),
                        rs.getInt("TradedMemeID"),
                        rs.getString("URL"),
                        rs.getInt("Upvotes"),
                        rs.getInt("Downvotes"));
                data.add(investedMeme);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    private ArrayList profileArrayList(ResultSet rs) {
        ArrayList<Profile> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (true) {

                if (!rs.next()) break;
                Profile profile = new Profile(
                        rs.getString("Username"),
                        rs.getString("Name"),
                        rs.getInt("Currency"));
                data.add(profile);
            }
        } catch (SQLException e) {
            infoDialog("" + e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    private ArrayList likedbyArrayList(ResultSet rs) {
        ArrayList<LikedBy> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                LikedBy like = new LikedBy(
                        rs.getInt("MemeID"),
                        rs.getString("ProfileUsername"),
                        rs.getBoolean("LikeDislike") ? "Like" : "Dislike",
                        rs.getString("MadeDate"));
                data.add(like);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }

        return data;
    }

    private ArrayList authorArrayList(ResultSet rs) {
        ArrayList<Author> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                Author author = new Author(
                        rs.getString("Username"),
                        rs.getInt("PostNumber"));
                data.add(author);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    private ArrayList viewAuthorArrayList(ResultSet rs) {
        ArrayList<ViAuthor> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                ViAuthor author = new ViAuthor(
                        rs.getString("Author"),
                        rs.getString("Name"),
                        rs.getInt("PostNumber"));
                data.add(author);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        return data;
    }

    private ArrayList templateArrayList(ResultSet rs) {
        ArrayList<Template> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                Template template = new Template(
                        rs.getInt("ID"),
                        rs.getString("Description"),
                        rs.getInt("Frequency"));
                data.add(template);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    private ArrayList investsArrayList(ResultSet rs) throws SQLException {
        ArrayList<Invests> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        while (rs.next()) {
            Invests invests = new Invests(
                    rs.getString("ProfileUsername"),
                    rs.getInt("TradedMemeID"),
                    rs.getInt("Amount"),
                    rs.getInt("Upvotes"),
                    rs.getInt("Downvotes"));
            data.add(invests);
        }
        return data;
    }

    private ArrayList tradedMemeArrayList(ResultSet rs) {
        ArrayList<TradedMeme> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                TradedMeme tradedMeme = new TradedMeme(
                        rs.getInt("MemeID"),
                        rs.getInt("IsPopular"));
                data.add(tradedMeme);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    private ArrayList madeByArrayList(ResultSet rs) {
        ArrayList<MadeBy> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                MadeBy madeBy = new MadeBy(
                        rs.getInt("MemeID"),
                        rs.getString("AuthorUsername"),
                        rs.getDate("MadeDate").toString());
                data.add(madeBy);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    private ArrayList bannedArrayList(ResultSet rs) {
        ArrayList<Banned> data = new ArrayList<>();
        if (rs == null) {
            return new ArrayList();
        }
        try {
            while (rs.next()) {
                Banned banned = new Banned(
                        rs.getString("Username"),
                        rs.getDate("BannedDate").toString());
                data.add(banned);
            }
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Query Error: " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
//            e.printStackTrace();
        }
        return data;
    }

    @FXML
    void onTradedClicked(ActionEvent event) {
        String memeId = tradedMemeText.getText().trim();

        if (!(memeId.chars().allMatch(Character::isDigit) && Integer.parseInt(memeId) >= 0)) {
            infoDialog("Meme ID is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
            return;
        }
        try {
            investTable.getColumns().clear();
            investTable.getItems().clear();
            buildAddTradedMemes(username, Integer.parseInt(memeId));
            //currencyText.setText();
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Error " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void onInvestClicked(ActionEvent event) {

        //Check for stuff
        String amount = investAmountText.getText();
        if (!(amount.chars().allMatch(Character::isDigit) && Integer.parseInt(amount) >= 0)) {
            infoDialog("Amount entered is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
            return;
        }
        String memeId = investIdText.getText();

        if (!(memeId.chars().allMatch(Character::isDigit) && Integer.parseInt(memeId) >= 0)) {
            infoDialog("Meme ID is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
            return;
        }
        try {
            investTable.getColumns().clear();
            investTable.getItems().clear();
            buildAddInvestment(username, Integer.parseInt(memeId), Integer.parseInt(amount));
            currencyText.setText(getCurrencyValue());
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Error " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
            return;
        }
    }

    @FXML
    void onReapClicked(ActionEvent event) {

        String memeId = reapText.getText();
        if (!(memeId.chars().allMatch(Character::isDigit) && Integer.parseInt(memeId) >= 0)) {
            infoDialog("Meme ID is invalid", "Invalid Input", "Error", Alert.AlertType.ERROR);
            return;
        }
        try {
            investTable.getColumns().clear();
            investTable.getItems().clear();
            buildReapInvestment(username, Integer.parseInt(memeId));
            currencyText.setText(getCurrencyValue());
        } catch (SQLException e) {
            infoDialog(e.getMessage(), "Error " + e.getErrorCode(), "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private String getCurrencyValue() {
        ResultSet rs;
        int currency = 0;
        try {
            String query = "SELECT Currency FROM Profile WHERE Username = ?";
            PreparedStatement ps = this.connection.getConnection().prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();

            while (rs.next()) {
                currency = rs.getInt("Currency");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getErrorCode() + ". " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return "" + currency;
    }

    boolean infoDialog(String message, String header, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        Optional<ButtonType> result = alert.showAndWait();

        if (alert.getAlertType().equals(Alert.AlertType.CONFIRMATION) && result.get() == ButtonType.OK) {
            System.out.println("OK Clicked");
            return true;
        }
        System.out.println("Cancel Clicked");
        return false;
    }


}
