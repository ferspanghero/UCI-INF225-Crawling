package crawling.core;

import java.util.HashSet;
import java.util.Map;

/**
 * Represents a processor that does a set of operations with the crawled pages
 */
public interface IPagesProcessor {

	/**
	 * Sets the crawled pages repository that should be processed
	 */
	void setRepository(IPagesRepository repository);

	/**
	 * Returns the number of unique crawled pages
	 */
	int getUniquePagesCount();

	/**
	 * Gets all crawled pages subdomains URLs and the number of unique detected
	 * in each subdomain
	 */
	Map<String, Integer> getSubdomains();

	/**
	 * Gets the crawled page with the longest plain text
	 */
	String getLongestPage();

	/**
	 * Gets the most common words found in the crawled pages plain text
	 * 
	 * @param count
	 *            The top N words that should be returned
	 * @param stopWords
	 *            A collection of words that should not be considered
	 */
	Map<String, Integer> getMostCommonWords(int count, HashSet<String> stopWords);

	/**
	 * Gets the most common N-grams found in the crawled pages plain text
	 *
	 * @param count
	 *            The top N N-grams that should be returned
	 * @param n
	 *            The type of N-grams that should be returned (i.e: 2-grams,
	 *            3-grams, etc.)
	 */
	Map<String, Integer> getMostCommonNGrams(int count, int n);
}
