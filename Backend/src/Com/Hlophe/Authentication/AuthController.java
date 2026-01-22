package Com.Hlophe.Authentication;

import java.sql.*;
import Com.Hlophe.Connection.Database;
import Com.Hlophe.OrphanageHome.JsonParser;
import Com.Hlophe.OrphanageHome.JsonParser.JsonObject;

public class AuthController {

    public static String login(String body) {

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parseObject(body);

        String username = json.getString("username");
        String password = json.getString("password");

        if (username == null || password == null) {
            return "{\"success\":false,\"message\":\"Missing credentials\"}";
        }

        String sql = "SELECT password_hash FROM users WHERE username=?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return "{\"success\":false,\"message\":\"Invalid credentials\"}";
            }

            String storedHash = rs.getString("password_hash");
            String incomingHash = PasswordUtil.hash(password);

            if (!storedHash.equals(incomingHash)) {
                return "{\"success\":false,\"message\":\"Invalid credentials\"}";
            }

            String token = TokenManager.generateToken(username);
            return "{\"success\":true,\"token\":\"" + token + "\"}";

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"Server error\"}";
        }
    }
}
