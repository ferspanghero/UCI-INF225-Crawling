package crawling.core;

import java.sql.SQLException;
import java.util.List;

/**
 * Represents a repository that contains data about the crawled pages
 */
public interface IPagesRepository {
	/**
	 * Sets the repository to read pages from the beginning
	 */
	void reset();

	/**
	 * Parses and inserts a page into the repository
	 * 
	 * @throws SQLException
	 */
	void insertPages(List<PageProcessingData> pages) throws SQLException;

	/**
	 * Retrieves the next page that can be sequentially iterated from the repository
	 * 
	 * @param pagesChunkSize
	 *            Determines the number of pages that should be retrieved
	 * @throws SQLException
	 */
	List<PageProcessingData> retrieveNextPages(int pagesChunkSize) throws SQLException;

	/**
	 * Deletes pages from the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Values indicating if the operations were performed successfully
	 */
	int[] deletePages(List<PageProcessingData> pages) throws SQLException;

	/**
	 * Clears the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Value indicating if the operation was performed successfully
	 */
	int clear() throws SQLException;
}
