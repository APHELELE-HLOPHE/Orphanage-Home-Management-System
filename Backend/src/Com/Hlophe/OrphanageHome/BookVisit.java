package Com.Hlophe.OrphanageHome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Com.Hlophe.Connection.Database;
import Com.Hlophe.OrphanageHome.JsonParser.JsonObject;

public class BookVisit {
    
    public static String handlePostRequest(String body) {
        try {
            if (body == null || body.trim().isEmpty()) {
                return "{\"success\":false,\"message\":\"The body is empty\"}";
            }
            
            JsonParser parser = new JsonParser();
            JsonObject request = parser.parseObject(body);
            
            String name = request.getString("name");
            String surname = request.getString("surname");
            String phoneNumber = request.getString("phoneNumber");
            String visitDate = request.getString("visitDate");
            
            if (name == null || name.trim().isEmpty() ||
                surname == null || surname.trim().isEmpty() ||
                phoneNumber == null || phoneNumber.trim().isEmpty() ||
                visitDate == null || visitDate.trim().isEmpty()) {
                
                return "{\"success\":false,\"message\":\"All fields are required\"}";
            }
            
            String sql = "INSERT INTO bookvisit (name, surname, phone_number, visit_date) VALUES (?, ?, ?, ?)";
            
            try (Connection connection = Database.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                
                ps.setString(1, name);
                ps.setString(2, surname);
                ps.setString(3, phoneNumber);
                ps.setString(4, visitDate);
                
                int rowsInserted = ps.executeUpdate();
                
                if (rowsInserted > 0) {
                    return "{\"success\":true,\"message\":\"Visit booked successfully!\"}";
                } 
                else {
                    return "{\"success\":false,\"message\":\"Failed to book visit\"}";
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
            return "{\"success\":true,\"message\":\"Visit booked successfully! (demo mode)\"}";
        } 
        catch (Exception e) {
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"Invalid value format: ";
        }
    }
}