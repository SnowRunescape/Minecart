package br.com.minecart.database;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.ConfigurationSection;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import br.com.minecart.MineCart;

public class Database {
    private final String connectionUri;
    private final String username;
    private final String password;
    
	private Connection connection;
	
	public Database(ConfigurationSection config) throws ClassNotFoundException, SQLException {
		final String hostname = config.getString("hostname", "localhost");
		final int port = config.getInt("port", 3306);
		final String database = config.getString("database", "");
		
		connectionUri = String.format("jdbc:mysql://%s:%d/%s", hostname, port, database);
		username = config.getString("username", "");
		password = config.getString("password", "");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			this.connect();
		} catch (SQLException sqlException) {
			this.close();
			
			throw sqlException;
		}
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
    public void close(){
    	try {
        	if(connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ignored){
        	
        }
    	
    	connection = null;
    }
    
    public boolean checkConnection(){
    	try {
    		connect();
    	} catch (SQLException sqlException){
    		this.close();
    		sqlException.printStackTrace();
    		
    		return false;
    	}
    	
    	return true;
    }
    
    public void createTables() throws IOException, SQLException {
    	URL resource = Resources.getResource(MineCart.class, "/tables.sql");
    	String[] databaseStructure = Resources.toString(resource, Charsets.UTF_8).split(";");

    	if(databaseStructure.length == 0) return;

    	Statement statement = null;

    	try {
    		connection.setAutoCommit(false);
    		statement = connection.createStatement();

    		for (String query : databaseStructure){
    			query = query.trim();

    			if (query.isEmpty()) continue;

    			statement.execute(query);
    		}

    		connection.commit();
    	} finally {
    		connection.setAutoCommit(true);

    		if(statement != null && !statement.isClosed()) statement.close();
    	}
    }
    
    private void connect() throws SQLException {
        if (connection != null){
            try {
                connection.createStatement().execute("SELECT 1;");

            } catch (SQLException sqlException){
                if(sqlException.getSQLState().equals("08S01")){
                    try {
                        connection.close();
                    } catch (SQLException ignored){
                    	
                    }
                }
            }
        }

        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(connectionUri, username, password);
        }
    }
}
