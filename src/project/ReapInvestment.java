package project;

import javax.swing.*;
import java.sql.*;

public class ReapInvestment {
    private DatabaseConnectionService dbService = null;

    public ReapInvestment(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet reapInvestment(String username, int ID) {

        String query = "SELECT * FROM Invests WHERE ProfileUsername = ?";

        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{call ReapInvestment(?, ?)}");
            cs.setString(1, username);
            cs.setInt(2, ID);

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
