package Com.Hlophe.Connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {

    public void writeQueries() {
        try {
            Connection _connection = Database.getConnection();
            Statement _statement = _connection.createStatement();
            
            String createChildrenTable = """
                CREATE TABLE IF NOT EXISTS children (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100),
                    surname VARCHAR(100),
                    age INT,
                    race VARCHAR(50)
                );
            """;
            
            String createDonationsTable = """
                CREATE TABLE IF NOT EXISTS donations (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    item_name VARCHAR(100),
                    quantity INT,
                    description TEXT
                );
            """;
            
            String createVisitsTable = """
                CREATE TABLE IF NOT EXISTS bookvisit (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    visitor_name VARCHAR(100),
                    visit_date DATE,
                    purpose TEXT
                );
            """;
            
            _statement.execute(createChildrenTable);
            _statement.execute(createDonationsTable);
            _statement.execute(createVisitsTable);
            
            String insertChildren = """
                INSERT INTO children (name, surname, age, race) VALUES
                ('Siphesihle', 'Mthembu', 15, 'African'),
                ('Peter', 'Smith', 9, 'White'),
                ('Njabulo', 'Zondo', 13, 'African'),
                ('Abenathi', 'Xulu', 5, 'African'),
                ('Smile', 'Gallant', 9, 'Indian');
                """;
            
            _statement.executeUpdate(insertChildren);
            _statement.close();
            
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}