package searchengine.core.repository;

import java.sql.SQLException;
import java.util.List;

import searchengine.core.PageProcessingData;

/**
 * Represents a repository that contains data about the crawled pages
 */
public interface IPagesRepository {
	/**
	 * Sets the repository to read pages from the beginning
	 */
	void reset();

	/**
	 * Retrieves the next pages that can be sequentially iterated from the repository
	 * 
	 * @param pagesChunkSize
	 *            Determines the number of pages that should be retrieved
	 * 
	 * @throws SQLException
	 */
	List<PageProcessingData> retrieveNextPages(int pagesChunkSize) throws SQLException;

	/**
	 * Inserts pages into the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Value indicating if the operation was performed successfully
	 */
	int[] insertPages(List<PageProcessingData> pages) throws SQLException;

	/**
	 * Updates pages in the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Value indicating if the operation was performed successfully
	 */
	int[] updatePages(List<PageProcessingData> pages) throws SQLException;

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
	int clearPages() throws SQLException;
}
