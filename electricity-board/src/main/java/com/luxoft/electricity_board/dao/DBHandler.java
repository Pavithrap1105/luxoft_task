package com.luxoft.electricity_board.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBHandler {

	public Connection establishConnection() throws Exception {
		FileInputStream fileInputStream = new FileInputStream("db.properties");
		Properties props = new Properties();
		props.load(fileInputStream);
		String driverClass = (String) props.get("db.classname");
		String url = (String) props.get("db.url");
		String username = (String) props.get("db.username");
		String password = (String) props.get("db.password");
		Class.forName(driverClass);
		Connection connection = DriverManager.getConnection(url, username, password);
		return connection;
	}

}
