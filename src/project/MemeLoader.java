package project;


import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class MemeLoader {
    private DatabaseConnectionService dbService = null;
    private File file;

    public MemeLoader(DatabaseConnectionService dbService, File file) {
        this.dbService = dbService;
        this.file = file;
    }

    public void loadMemes() {

        Scanner sc = null;

        Connection con = this.dbService.getConnection();

        try {
            sc = new Scanner(this.file);
            sc.useDelimiter(",");
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            return;
        }

        while (sc.hasNext()) {
//            System.out.println("1: "+sc.next());
//            System.out.println("2: "+sc.next());
//            System.out.println("3: "+sc.next());
//            System.out.println("4: "+sc.next());
//            System.out.println("5: "+sc.next());
//            System.out.println("6: "+sc.next());
            try {
                CallableStatement cs = con.prepareCall("{call [Add Meme](?, ?, ?, ?, ?, ?)}");

                cs.setString(1, sc.next());
                cs.setInt(2, Integer.parseInt(sc.next()));
                cs.setString(3, sc.next());
                cs.setString(4, sc.next());
                cs.setString(5, sc.next());
                cs.setString(6, sc.next());


                cs.execute();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error " + e.getErrorCode() + ". " + e.getMessage());
                return;
            }
        }
    }
}
