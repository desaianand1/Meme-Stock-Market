package project;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class AddProfile {
    private DatabaseConnectionService dbService = null;

    public AddProfile(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    //adds the profile using the stored procedure in the database, returns the result set of a select statement on the new profile
    public ResultSet addProfile(String username, String name) {
        if (name == null){
            name = "Leroy Jenkins";
        }

        String query = "SELECT * FROM Profile WHERE Username = ?";

        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{call Add ViAuthor(?, ?)}");
            cs.setString(1, username);
            cs.setString(2, name);

            cs.execute();

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);

            rs = ps.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }

        return rs;
    }
}
