package project;

import javax.swing.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class GetMemesByAuthor {

    private DatabaseConnectionService dbService = null;

    public GetMemesByAuthor(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet getMemesByAuthor(String author) {
        String call = "{call dbo.GetMemesByAuthor (?) }";
        ResultSet rs = null;

        try {
            CallableStatement proc = this.dbService.getConnection().prepareCall(call);
            //proc.registerOutParameter(1, Types.INTEGER);
            proc.setString(1, author);
            rs = proc.executeQuery();

//            int returnVal = proc.getInt(1);
//            if (returnVal == 1) {
//                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". No creator defined");
//                return null;
//            } else if (returnVal == 2) {
//                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". User is not a valid author");
//                return null;
//            }

            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
