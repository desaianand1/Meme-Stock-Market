package project;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;


public class LoginService {
    private static final Random RANDOM = new SecureRandom();
    private static final Base64.Encoder enc = Base64.getEncoder();
    private static final Base64.Decoder dec = Base64.getDecoder();
    private DatabaseConnectionService dbService;

    public LoginService(DatabaseConnectionService dbService) {
        this.dbService = dbService;
    }

    public boolean login(String username, String password) {
        PreparedStatement getLoginHashSalt;
        Connection con = this.dbService.getConnection();
        String passHash = "";
        String passSalt = "";
        try {
            String stmt = "SELECT PasswordSalt,PasswordHash \nFROM Login\nWHERE ProfileUsername=?";
            getLoginHashSalt = con.prepareStatement(stmt);
            getLoginHashSalt.setString(1, username);
            ResultSet rs = getLoginHashSalt.executeQuery();
            while (rs.next()) {
                passHash = rs.getString("PasswordHash");
                passSalt = rs.getString("PasswordSalt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String hash = hashPassword(passSalt.getBytes(), password);
        if (hash.equals(passHash)) {
            return true;
        }
        JOptionPane.showMessageDialog(null, "Login Failed.");
        return false;
    }

    public boolean register(String username, String password, String name) {
        byte[] salt = getNewSalt();
        String saltString = getStringFromBytes(salt);
        String hash = hashPassword(salt, password);
        try {
            CallableStatement cs = this.dbService.getConnection().prepareCall("{? = call Register(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            cs.setString(3, hash);
            cs.setString(4, saltString);
            cs.setString(5,name);
            cs.execute();
            int returnVal = cs.getInt(1);

            if (returnVal == 1 || returnVal == 2) {
                JOptionPane.showMessageDialog(null, "Error: " + returnVal + ". Registration Failed.");
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public byte[] getNewSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public String getStringFromBytes(byte[] data) {
        return enc.encodeToString(data);
    }

    public String hashPassword(byte[] salt, String password) {

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f;
        byte[] hash = null;
        try {
            f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
            e.printStackTrace();
        }
        return getStringFromBytes(hash);
    }
}
