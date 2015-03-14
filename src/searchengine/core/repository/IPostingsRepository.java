package searchengine.core.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import searchengine.core.IndexPosting;

/**
 * Represents a repository that contains data about the crawled pages terms index postings
 */
public interface IPostingsRepository {
	/**
	 * Sets the repository to read postings from the beginning
	 */
	void reset();

	/**
	 * Retrieves the next postings that can be sequentially iterated from the repository
	 * 
	 * @param postingsChunkSize
	 *            Determines the number of postings that should be retrieved
	 * 
	 * @throws SQLException
	 */
	List<IndexPosting> retrieveNextPostings(int postingsChunkSize) throws SQLException;

	/**
	 * Inserts postings into the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Value indicating if the operation was performed successfully
	 */
	int insertPostings(List<IndexPosting> postings) throws SQLException;

	/**
	 * Updates postings into the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Value indicating if the operation was performed successfully
	 */
	int[] updatePostings(List<IndexPosting> postings) throws SQLException;

	/**
	 * Deletes postings from the repository
	 * 
	 * @throws SQLException
	 * 
	 * @return Values indicating if the operations were performed successfully
	 */
	int[] deletePostings(List<IndexPosting> postings) throws SQLException;

	/**
	 * Calculates the ranking scores of all index postings
	 * 
	 * @throws SQLException
	 */
	void buildIndexRankingData() throws SQLException;

	/**
	 * Retrieves the words pages positions
	 * 
	 * @param words
	 *            the words that should be considered
	 * 
	 * @return A map where the key is a page repository id and the value is a sorted word positions collection
	 * 
	 * @throws SQLException
	 */
	Map<Integer, List<Integer>> retrieveWordsPagesPositions(List<String> words) throws SQLException;
}
