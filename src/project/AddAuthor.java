package project;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class AddAuthor {
    private DatabaseConnectionService dbService = null;

    public AddAuthor(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    //adds a new author using the stored procedure in the database, returns a result set of a select statement on the author
    public ResultSet addAuthor(String username) {

        String query = "SELECT * FROM ViAuthor WHERE Username = ?";

        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{call Add ViAuthor(?)}");
            cs.setString(1, username);

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
