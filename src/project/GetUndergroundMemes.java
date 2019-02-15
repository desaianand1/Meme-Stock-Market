package project;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetUndergroundMemes {
    private DatabaseConnectionService dbService = null;

    public GetUndergroundMemes(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    //grabs all memes with at least one investor, ordered by lowest number of investors. Returns the result set of this query
    public ResultSet getUndergroundMemes() {
        ResultSet rs;
        String query = "EXEC dbo.GetUndergroundMemes;";
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
