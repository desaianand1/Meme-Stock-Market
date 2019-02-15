package project;

import javax.swing.*;
import java.sql.*;

public class AddTradedMeme {

    private DatabaseConnectionService dbService = null;

    public AddTradedMeme(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet addTradedMeme(String username, int ID) {

        String query = "SELECT * FROM TradedMeme WHERE MemeID = ?";


        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{call AddTradedMeme(?, ?)}");
            cs.setString(1, username);
            cs.setInt(2, ID);

            cs.execute();

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, ID);

            rs = ps.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getErrorCode() + ". " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return rs;
    }
}
