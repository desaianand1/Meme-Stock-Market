package project;

import javax.swing.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetMemesByInvestorCount {
    private DatabaseConnectionService dbService = null;

    public GetMemesByInvestorCount(DatabaseConnectionService dbService){
        this.dbService = dbService;
    }

    public ResultSet getMemesByInvestors(int num){
        Connection con = this.dbService.getConnection();
        ResultSet rs = null;

        try {
            CallableStatement cs = con.prepareCall("{call dbo.GetMemesByNumberOfInvestments(?)}");
            cs.setInt(1, num);

            rs = cs.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }

        return rs;
    }
}
