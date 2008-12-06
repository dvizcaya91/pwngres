package tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

	private final String host;
	private final String username;
	private final String password;
	private final String database;
	
	private Connection connection;
	
	/**
	 * Constructor of a PostgresConnection object.
	 *
	 * @param host : The database's host. Needs to be in the following 
	 * 		format: //host:port/
	 * @param username : The username that will connect to the database
	 * @param password : The password of the given username
	 * @param database : Name of the database to which we are connecting
	 */
	public PostgresConnection(String host, String username, 
			String password, String database) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
		connect();
	}
	
	/**
	 * Attempts to register the Postgres JDBC driver. Then 
	 * it attempts to connect to the database.
	 */
	private void connect() {
		
		try {
			//System.out.println("Registering driver...");
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Couldn't find driver!");
			cnfe.printStackTrace();
			System.exit(1);
		}

		Connection c = null;

		//System.out.println("Connecting to database...");
		
		try {		
			c = DriverManager.getConnection("jdbc:postgresql:" + host + database,
					username, password);
		} catch (SQLException se) {
			System.out.println("Error when trying to connect to database");
			se.printStackTrace();
			System.exit(1);
		}
		
		if (c == null) {
			System.out.println("Database connection was not successful");
			System.exit(1);
		}
		
		//System.out.println("Connection to database successful!");
		
		this.connection = c; 
	}
	
	/**
	 * Returns a Connection to the Postgres database 
	 * @return connection to the Postgres database 
	 */
	public Connection getConnection() {
		return connection;
	}
	
}
