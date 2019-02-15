package project;

import javax.swing.*;
import java.sql.*;

public class GetInvestmentsForProfile {
    private DatabaseConnectionService dbService = null;

    public GetInvestmentsForProfile(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet getInvestments(String username) {

        Connection con = this.dbService.getConnection();
        ResultSet rs;

        try {
            CallableStatement cs = con.prepareCall("{call GetInvestmentsForProfile(?)}");
            cs.setString(1, username);
            
            rs = cs.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }

        return rs;
    }
}
