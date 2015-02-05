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
	 * Parses and inserts a page into the pages repository
	 * 
	 * @throws SQLException
	 */
	void insertPages(List<PageProcessingData> pages) throws SQLException;

	/**
	 * Retrieves the next page that can be sequentially iterated from the pages
	 * repository
	 * 
	 * @param pagesChunkSize
	 *            Determines the number of pages that should be retrieved
	 * @throws SQLException
	 */
	List<PageProcessingData> retrieveNextPages(int pagesChunkSize) throws SQLException;
}
