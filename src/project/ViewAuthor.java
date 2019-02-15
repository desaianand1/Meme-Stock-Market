package project;

import javax.swing.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ViewAuthor {

    private DatabaseConnectionService dbService = null;

    public ViewAuthor(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet getAuthor(String author) {
        String call = "{call dbo.[View ViAuthor] (?) }";
        ResultSet rs = null;

        try {
            CallableStatement proc = this.dbService.getConnection().prepareCall(call);
            //proc.registerOutParameter(1, Types.INTEGER);
            proc.setString(1, author);
            rs = proc.executeQuery();

//            int returnVal = proc.getInt(1);
//            if (returnVal ==1) {
//                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". No creator defined");
//            }
//            else if(returnVal==2){
//                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". User is not a valid author");
//            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return rs;
    }
}
