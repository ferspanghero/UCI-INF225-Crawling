package searchengine.core;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import searchengine.core.repository.IRepositoriesFactory;

/**
 * Represents a basic builder of the pages terms index
 */
public class DefaultIndexBuilder implements IIndexBuilder {
	private final static int POSTINGS_CHUNK_SIZE = 1024;

	@Override
	public void buildIndex(IRepositoriesFactory repositoriesFactory) throws ClassNotFoundException, SQLException {
		if (repositoriesFactory == null)
			throw new IllegalArgumentException("The pages processor cannot be initialized with a null repositories factory");

		int pagesCount = repositoriesFactory.getPagesRepository().retrievePagesCount();

		if (pagesCount > 0) {
			Map<Integer, Integer> wordsPagesFrequencies = repositoriesFactory.getPostingsRepository().retrieveWordsPagesFrequencies();

			if (wordsPagesFrequencies != null && wordsPagesFrequencies.size() > 0) {
				// Makes sure the pages iteration will be from the beginning
				repositoriesFactory.getPostingsRepository().reset();
				
				List<IndexPosting> postings = repositoriesFactory.getPostingsRepository().retrieveNextPostings(POSTINGS_CHUNK_SIZE);

				while (postings != null && postings.size() > 0) {
					for (IndexPosting posting : postings) {
						// Calculates the TF-IDF through the formula: (wordFrequencyInPageN * totalNumberOfPages) / totalNumberOfPagesWordOcurrsIn
						posting.setTfIdf((posting.getWordFrequency() * pagesCount) / wordsPagesFrequencies.get(posting.getWordId()));
					}

					repositoriesFactory.getPostingsRepository().updatePostings(postings);

					postings = repositoriesFactory.getPostingsRepository().retrieveNextPostings(POSTINGS_CHUNK_SIZE);
				}
			}
		}
	}
}
