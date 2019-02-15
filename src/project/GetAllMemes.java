package project;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetAllMemes {
    private DatabaseConnectionService dbService = null;

    public GetAllMemes(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    //grabs all memes in the Meme table, returns result set of all memes
    public ResultSet getMemes() {
        ResultSet rs;
        String query = "SELECT ID, Phrase, AgeRestricted,URL,ExpDate, Upvotes, Downvotes FROM Meme ORDER BY ID ASC";
        try {
            Statement statement = this.dbService.getConnection().createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }
        return rs;
    }
}
