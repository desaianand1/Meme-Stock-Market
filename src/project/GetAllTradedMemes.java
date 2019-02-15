package project;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class GetAllTradedMemes {

    private DatabaseConnectionService dbService = null;

    public GetAllTradedMemes(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    //grabs all memes in the TradedMeme table, returns result set of all traded memes
    public ResultSet getTradedMemes() {
        ResultSet rs;
        String query = "SELECT MemeID, Phrase, URL, Upvotes, Downvotes FROM Meme JOIN TradedMeme ON MemeID = ID";
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
