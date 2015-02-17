package searchengine.core;

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
	 * Inserts a page into the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Value indicating if the operation was performed successfully
	 */
	int insertPage(PageProcessingData page) throws SQLException;

	/**
	 * Retrieves the next page that can be sequentially iterated from the repository
	 * 
	 * @param pagesChunkSize
	 *            Determines the number of pages that should be retrieved
	 * @throws SQLException
	 */
	List<PageProcessingData> retrieveNextPages(int pagesChunkSize) throws SQLException;

	/**
	 * Clears the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Value indicating if the operation was performed successfully
	 */
	int clear() throws SQLException;
}
