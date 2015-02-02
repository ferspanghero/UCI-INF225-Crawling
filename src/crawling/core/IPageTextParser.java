package crawling.core;

import edu.uci.ics.crawler4j.crawler.Page;

/**
 * Represents a parser that extracts the plain text from crawled pages
 */
public interface IPageTextParser {
	/**
	 * Parses a crawled page and returns its plain text
	 */
	String parsePage(Page page);
}
