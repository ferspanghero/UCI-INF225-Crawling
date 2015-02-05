package crawling.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.Page;

/**
 * Represents a MySql database that contains data about the crawled pages
 */
public class MySqlPagesRepository implements IPagesRepository {
	@Override
	public void reset() {

	}

	@Override
	public void insertPage(Page page) {
		// TODO Think about bulk insert
	}

	@Override
	public List<PageProcessingData> retrieveNextPages() {
		return null;
	}

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@Override
	public List<PageProcessingData> retrieveNextPages(int pagesChunkSize) {
		/*// This will load the MySQL driver, each DB has its own driver
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root");

		// Statements allow to issue SQL queries to the database
		statement = connect.createStatement();
		// Result set get the result of the SQL query
		resultSet = statement.executeQuery("select * from feedback.comments");*/

		return null;
	}
}
