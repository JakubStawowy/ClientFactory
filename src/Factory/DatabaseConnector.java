package Factory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DatabaseConnector {

    private Connection connection;
    private static DatabaseConnector instance;

    private DatabaseConnector(String host, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(host, username, password);
    }

    public static DatabaseConnector getInstance() throws SQLException {
        if(instance == null)
            instance = new DatabaseConnector("jdbc:mysql://localhost:3306/FactoryDB", "root", "");
        return instance;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public void execute(String query) throws SQLException{
        connection.createStatement().execute(query);
    }

    public void disconnect()throws SQLException{
        connection.close();
    }
}