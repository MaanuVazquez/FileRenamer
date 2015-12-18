package ar.manumaanu.database;

import java.sql.*;

public class Database {

	public Connection connection;
	public String dbDirectory;

	public Database(String dbDirectory) {

		this.connection = null;

		try {
			Class.forName("org.sqlite.JDBC");
			this.dbDirectory = dbDirectory;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	public void createTable() {

		Statement statement = null;

		try {
			openConnection();
			statement = this.connection.createStatement();
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS RENAME (TYPE INTEGER NOT NULL, RENAME TEXT NOT NULL, SERIE TEXT NOT NULL)");
			statement.close();
			closeConnection();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	public void insert(int type, String rename, String serie) {

		Statement statement = null;

		try {
			openConnection();
			this.connection.setAutoCommit(false);
			statement = this.connection.createStatement();
			statement.executeUpdate("INSERT INTO RENAME VALUES (" + type + ", '" + rename + "', '" + serie + "')");
			this.connection.commit();
			statement.close();
			closeConnection();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	public ResultSet getResultSet() {

		Statement statement = null;
		ResultSet resultSet = null;

		try {
			openConnection();
			statement = this.connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RENAME");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("ResultSet created succesfully");
		return resultSet;

	}

	public void optimize() {
		Statement statement = null;

		try {
			openConnection();
			statement = this.connection.createStatement();
			statement.executeUpdate("VACUUM FULL");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Database optimized succesfully");
	}

	public void openConnection() {

		this.connection = null;

		try {
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbDirectory);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	public void closeConnection() {
		try {

			this.connection.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Connection closed succesfully");
	}

}
