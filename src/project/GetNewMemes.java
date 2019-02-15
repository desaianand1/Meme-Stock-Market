package project;

import javax.swing.*;
import java.sql.*;

public class GetNewMemes {
    private DatabaseConnectionService dbService = null;

    public GetNewMemes(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public ResultSet getNewMemes() {
        ResultSet rs;
        String query = "EXECUTE dbo.SortMemesByDateNew;";
        try {
            Statement statement = this.dbService.getConnection().createStatement();
            rs = statement.executeQuery(query);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return null;
        }
        return rs;
    }
}
