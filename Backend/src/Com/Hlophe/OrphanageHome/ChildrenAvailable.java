package Com.Hlophe.OrphanageHome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Com.Hlophe.Connection.Database;

public class ChildrenAvailable {
    
    public static String handleGetRequest() {
        List<Children> children = new ArrayList<>();
        
        String query = "SELECT * FROM children";
        
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Children child = new Children();
                child.setId(rs.getInt("id"));
                child.setName(rs.getString("name"));
                child.setSurname(rs.getString("surname"));
                child.setAge(rs.getInt("age"));
                child.setRace(rs.getString("race"));
                
                children.add(child);
            }
            
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < children.size(); i++) {
                json.append(children.get(i).toJson());
                if (i < children.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            
            return json.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}