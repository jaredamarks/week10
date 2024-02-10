package projects;

import java.sql.Connection;

import projects.dao.DbConnection;

@SuppressWarnings("unused")
public class Projects {

	public static void main(String[] args) {
		DbConnection.getConnection();
	}

}
