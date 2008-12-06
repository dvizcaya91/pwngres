package utils;

import tool.PostgresConnection;

/**
 * A class with a main() to test stuff
 * 
 * @author julian
 *
 */
public class Test {


	public static void main(String[] args) {

		String host = "//localhost:5432/";
		String username = "postgres";
		String password = "postgres";
		String database = "test";

		PostgresConnection pgconnect = new PostgresConnection(
				host, username, password, database);
		
	}

}
