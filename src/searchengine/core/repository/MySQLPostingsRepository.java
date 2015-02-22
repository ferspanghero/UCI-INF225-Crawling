package searchengine.core.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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
		return null;
	}

	@Override
	public int insertPostings(List<IndexPosting> postings) throws SQLException {
		return -1;
	}

	@Override
	public int[] updatePostings(List<IndexPosting> postings) throws SQLException {
		int[] updateCounts = null;

		if (postings != null) {
			try (Connection connection = getConnection()) {
				try (PreparedStatement statement = connection.prepareStatement("UPDATE wordspages SET TFIDF = ? WHERE WordId = ? AND PageId = ?")) {
					for (IndexPosting posting : postings) {
						statement.setDouble(1, posting.getTfIdf());
						statement.setInt(2, posting.getWordId());
						statement.setInt(3, posting.getPageId());

						statement.addBatch();
					}

					updateCounts = statement.executeBatch();
				}
			}
		}

		return updateCounts;
	}

	@Override
	public int[] deletePostings(List<IndexPosting> postings) throws SQLException {
		int[] deleteCountArray = null;

		if (postings != null) {
			try (Connection connection = getConnection()) {
				try (PreparedStatement statement = connection.prepareStatement("DELETE FROM words WHERE Id = ?")) {
					for (IndexPosting posting : postings) {
						statement.setInt(1, posting.getWordId());

						statement.addBatch();
					}

					deleteCountArray = statement.executeBatch();
				}
			}
		}

		return deleteCountArray;
	}

	@Override
	public Map<Integer, Integer> retrieveWordsPagesFrequencies() throws SQLException {
		Map<Integer, Integer> wordsPagesFrequencies = new HashMap<Integer, Integer>();

		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				String sql = "SELECT WordId, COUNT(*) FROM wordspages GROUP BY WordId";

				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						wordsPagesFrequencies.put(resultSet.getInt(1), resultSet.getInt(2));
					}
				}
			}
		}

		return wordsPagesFrequencies;
	}

	private Connection getConnection() throws SQLException {
		// TODO: make connection parameters configurable
		// useServerPrepStmts=false tells MySQL to handle server-side prepared statements locally
		// rewriteBatchedStatements=true tells MySQL to pack as many queries as possible into a single network packet
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/ucicrawling?user=root&password=password&useServerPrepStmts=false&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8");
	}
}
