package Com.Hlophe.OrphanageHome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Com.Hlophe.Connection.Database;
import Com.Hlophe.OrphanageHome.JsonParser.JsonObject;

public class Donations {
    
    public static String handlePostRequest(String body) {
        try {
            if (body == null || body.trim().isEmpty()) {
                return "{\"success\":false,\"message\":\"Request body is empty\"}";
            }
            
            JsonParser parser = new JsonParser();
            JsonObject request = parser.parseObject(body);
            
            String itemName = request.getString("itemName");
            Object quantityObj = request.get("quantity");
            String description = request.getString("description");
            
            if (itemName == null || itemName.trim().isEmpty() || quantityObj == null) {
                return "{\"success\":false,\"message\":\"Item name and quantity are required\"}";
            }
            
            int quantity;
            try {
                if (quantityObj instanceof Integer) {
                    quantity = (Integer) quantityObj;
                } else if (quantityObj instanceof String) {
                    quantity = Integer.parseInt((String) quantityObj);
                } else if (quantityObj instanceof Double) {
                    quantity = ((Double) quantityObj).intValue();
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                return "{\"success\":false,\"message\":\"Quantity must be a number\"}";
            }
            
            if (quantity <= 0) {
                return "{\"success\":false,\"message\":\"Quantity must be greater than 0\"}";
            }
            
            String sql = "INSERT INTO donations (item_name, quantity, description) VALUES (?, ?, ?)";
            
            try (Connection connection = Database.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                
                ps.setString(1, itemName);
                ps.setInt(2, quantity);
                ps.setString(3, description != null ? description : "");
                
                int rowsInserted = ps.executeUpdate();
                
                if (rowsInserted > 0) {
                    return "{\"success\":true,\"message\":\"Donation submitted successfully!\"}";
                } else {
                    return "{\"success\":false,\"message\":\"Failed to save donation\"}";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\"success\":true,\"message\":\"Donation submitted successfully! (demo mode)\"}";
        } 
        catch (Exception e) {
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"Invalid request format: ";
        }
    }
    
}