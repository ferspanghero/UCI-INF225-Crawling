package crawling.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a MySql database that contains data about the crawled pages
 */
// TODO: this class is not currently testable. Some parameters need to be
// injected
public class MySQLPagesRepository implements IPagesRepository {
	public MySQLPagesRepository() throws ClassNotFoundException {
		// Loads the MySQL driver
		Class.forName("com.mysql.jdbc.Driver");

		reset();
	}

	// Since the database records are read in chunks (or pages), determines the
	// current page that will be read
	private int currentPagesPaginationIndex;

	@Override
	public void reset() {
		currentPagesPaginationIndex = 0;
	}

	@Override
	public void insertPages(List<PageProcessingData> pages) throws SQLException {
		if (pages != null) {
			try (Connection connection = getConnection()) {
				try (PreparedStatement statement = connection.prepareStatement("INSERT INTO CrawledPages VALUES (?, ?, ?)")) {
					for (PageProcessingData page : pages) {
						statement.setString(1, page.getUrl());
						statement.setString(2, page.getText());
						statement.setString(3, page.getHtml());

						statement.addBatch();
					}

					statement.executeBatch();
				}
			}
		}
	}

	@Override
	public List<PageProcessingData> retrieveNextPages(int pagesChunkSize) throws SQLException {
		List<PageProcessingData> pages = new ArrayList<PageProcessingData>();

		try (Connection connection = getConnection()) {
			// ResultSet.TYPE_SCROLL_SENSITIVE tells the driver to consider
			// altered records since the last page was read
			// ResultSet.CONCUR_READ_ONLY tells the driver to create a read-only
			// result set
			try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
				// Tells the drivers the expected result set size in advance to
				// enhance performance
				statement.setFetchSize(pagesChunkSize);
				statement.setMaxRows(pagesChunkSize);

				String sql = "SELECT URL, Text FROM CrawledPages LIMIT " + currentPagesPaginationIndex + ", " + pagesChunkSize;

				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						// TODO: for this version, the page's HTML content is
						// not being read from the DB
						PageProcessingData page = new PageProcessingData(resultSet.getString("URL"), resultSet.getString("Text"), "");

						pages.add(page);
					}
				}
			}
		}

		return pages;
	}

	private Connection getConnection() throws SQLException {
		// TODO: make connection parameters configurable
		// useServerPrepStmts=false tells MySQL to handle server-side prepared
		// statements locally
		// rewriteBatchedStatements=true tells MySQL to pack as many queries as
		// possible into a single network packet
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/UCICrawling/?user=root&useServerPrepStmts=false&rewriteBatchedStatements=true");
	}
}
