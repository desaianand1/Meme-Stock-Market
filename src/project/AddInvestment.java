package project;

import javax.swing.*;
import java.sql.*;

public class AddInvestment {
    private DatabaseConnectionService dbService = null;

    public AddInvestment(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet addInvestment(String username, int ID, int amount) {

        String query = "SELECT * FROM Invests WHERE ProfileUsername = ?";


        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{call AddInvestment(?, ?, ?)}");
            cs.setString(1, username);
            cs.setInt(2, ID);
            cs.setInt(3, amount);

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
