package searchengine.core.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import searchengine.core.IndexPosting;

/**
 * Represents a MySql database that contains data about the index postings
 */
// TODO: this class is not unit-testable. Some parameters need to be injected
public class MySQLPostingsRepository implements IPostingsRepository {
	public MySQLPostingsRepository() throws ClassNotFoundException {
		// Loads the MySQL driver
		Class.forName("com.mysql.jdbc.Driver");

		reset();
	}

	// Since the database records are read in chunks (or pages), determines the
	// current posting that will be read
	private int currentPostingsPaginationIndex;

	@Override
	public void reset() {
		currentPostingsPaginationIndex = 0;
	}

	@Override
	public List<IndexPosting> retrieveNextPostings(int postingsChunkSize) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] insertPostings(List<IndexPosting> postings) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int[] updatePostings(List<IndexPosting> postings) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<Integer, Integer> retrieveWordsPagesFrequencies() {
		// TODO Auto-generated method stub
		return null;
	}

	private Connection getConnection() throws SQLException {
		// TODO: make connection parameters configurable
		// useServerPrepStmts=false tells MySQL to handle server-side prepared statements locally
		// rewriteBatchedStatements=true tells MySQL to pack as many queries as possible into a single network packet
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/ucicrawling?user=root&password=password&useServerPrepStmts=false&rewriteBatchedStatements=true");
	}
}
