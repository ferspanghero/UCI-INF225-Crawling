package crawling.core;

import edu.uci.ics.crawler4j.crawler.Page;

/**
 * Represents a repository that contains data about the crawled pages
 */
public interface IPagesRepository {
	/**
	 * Parses and inserts a page into the pages repository
	 */
	void insertPage(Page page, IPageTextParser parser);

	/**
	 * Retrieves the next page that can be sequentially iterated from the pages
	 * repository
	 * 
	 * @param reset
	 *            Determines if the iteration should be reseted
	 */
	Page retrieveNextPage(Boolean reset);
}
