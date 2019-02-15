package project;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionService {

    private Connection connection = null;
    private String databaseName;
    private String serverName;
    private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    //this will be instantiated in main with default values
    public DatabaseConnectionService(String serverName, String databaseName) {
        this.serverName = serverName;
        this.databaseName = databaseName;
    }

    //this will be called in main with basic java input boxes for the user to login initially
    public boolean connect(String user, String pass) {
        String connectionUrl = String.format("jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;",
                this.serverName,
                this.databaseName,
                user,
                pass);

        System.out.println("Connected with URL: " + connectionUrl.replace(pass,"***********"));

        try {
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }
            this.connection = DriverManager.getConnection(connectionUrl);
            return true;
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error: "+e.getErrorCode()+". "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

