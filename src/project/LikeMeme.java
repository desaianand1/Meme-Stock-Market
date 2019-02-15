package project;

import javafx.application.Preloader;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.sql.*;

public class LikeMeme {
    private DatabaseConnectionService dbService = null;

    public LikeMeme(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet likeMeme(String profileName, boolean liked, int memeID) {

        String query = "SELECT MemeID, ProfileUsername, [LikeDislike] AS LikeDislike, MadeDate FROM LikedBy WHERE MemeID = ? AND ProfileUsername = ?";

        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{?= call likeMeme(?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, profileName);
            cs.setBoolean(3, liked);
            cs.setInt(4, memeID);
            cs.execute();

            int returnVal = cs.getInt(1);
            if (returnVal == 1) {
                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". Profile " + profileName + " does not exist in the Profile table");
            } else if (returnVal == 2) {
                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". Meme ID " + memeID + " does not exist in the Meme table");
            } else if (returnVal == 3) {
                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". Must enter 1 or 0 to Like/Dislike a meme, you entered: " + liked);
            } else if (returnVal == 4) {
                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". User " + profileName + " has already liked meme with Meme ID: " + memeID);
            }

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, memeID);
            ps.setString(2, profileName);

            rs = ps.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }

        return rs;
    }
}
