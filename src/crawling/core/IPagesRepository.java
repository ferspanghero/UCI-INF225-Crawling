package crawling.core;

import java.util.List;

import edu.uci.ics.crawler4j.crawler.Page;

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
	 */
	void insertPage(Page page);

	/**
	 * Retrieves the next page that can be sequentially iterated from the pages
	 * repository
	 */
	List<PageProcessingData> retrieveNextPages();

	/**
	 * Retrieves the next page that can be sequentially iterated from the pages
	 * repository
	 * 
	 * @param pagesChunkSize
	 *            Determines the number of pages that should be retrieved
	 */
	List<PageProcessingData> retrieveNextPages(int pagesChunkSize);
}
