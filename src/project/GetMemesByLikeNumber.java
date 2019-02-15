package project;

import javax.swing.*;
import java.sql.*;

public class GetMemesByLikeNumber {

    private DatabaseConnectionService dbService = null;

    public GetMemesByLikeNumber(DatabaseConnectionService dbService){
        this.dbService = dbService;
    }

    public ResultSet getMemesByLikeNumber(int num){
        Connection con = this.dbService.getConnection();
        ResultSet rs = null;

        try {
            CallableStatement cs = con.prepareCall("{call dbo.GetMemesByLikeNumber(?)}");
            //cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(1, num);

//            int returnVal = cs.getInt(1);
//            if (returnVal == 1) {
//                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". You must input a number");
//            }

            rs = cs.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }

        return rs;
    }
}
