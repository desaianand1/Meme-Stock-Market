package project;

import javax.swing.*;
import java.sql.*;

public class AddMeme {

    private DatabaseConnectionService dbService = null;

    public AddMeme(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    //adds the meme using the stored procedure in the database, returns the result set from running a select on the created meme
    public ResultSet addMeme(String phrase, boolean ageRestricted, String url, String author) {
        int realAgeRestricted = 0;
        if (ageRestricted){
            realAgeRestricted = 1;
        }

        String query = "SELECT * FROM Meme WHERE [URL] = ?";



        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{call [Add Meme](?, ?, ?, ?)}");
            cs.setString(1, phrase);
            cs.setInt(2, realAgeRestricted);
            cs.setString(3, url);
            cs.setString(4, author);

            cs.execute();

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, url);

            rs = ps.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }

        return rs;
    }


}
