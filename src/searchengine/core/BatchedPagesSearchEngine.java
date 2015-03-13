package searchengine.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import searchengine.core.repository.IRepositoriesFactory;

/**
 * Represents a basic engine that searches for pages
 */
public class BatchedPagesSearchEngine implements IPagesSearchEngine {
	public BatchedPagesSearchEngine(int pagesPerBatch) {
		setPagesPerBatch(pagesPerBatch);
		setPagesBatchIndex(1);

		pagesUrls = null;
		lastSearchedWords = new ArrayList<String>();
	}

	private int pagesPerBatch;
	private int pagesBatchIndex;	
	private List<String> pagesUrls;
	private List<String> lastSearchedWords;

	@Override
	public List<String> search(IRepositoriesFactory repositoriesFactory, String query) throws ClassNotFoundException, SQLException {
		List<String> pagesUrlsBatch = null;

		if (repositoriesFactory == null)
			throw new IllegalArgumentException("The search engine cannot be initialized with a null repositories factory");

		List<String> words = new ArrayList<String>(10);

		// TODO: Refactor stop words logic to be inside the Tokenizer so that it can be reused here
		if (query != null && !query.isEmpty()) {
			Tokenizer tokenize = new Tokenizer(query);
			while (tokenize.processNextToken()) {
				words.add(tokenize.getCurrentToken());
			}
		}

		if (!words.isEmpty()) {
			if (!lastSearchedWords.equals(words)) {
				pagesUrls = repositoriesFactory.getPagesRepository().searchPages(words);

				lastSearchedWords = words;
			}		
		}
		
		if (pagesUrls != null) {
			int startIndex = (getPagesBatchIndex() - 1) * getPagesPerBatch();
			int endIndex = startIndex + getPagesPerBatch();

			pagesUrlsBatch = pagesUrls.subList(startIndex, Math.min(endIndex, pagesUrls.size()));
		}

		return pagesUrlsBatch;
	}

	public int getPagesPerBatch() {
		return pagesPerBatch;
	}

	public void setPagesPerBatch(int resultsPerBatch) {
		if (resultsPerBatch <= 0)
			throw new IllegalArgumentException("The pages number per batch has to be greater than 0");

		this.pagesPerBatch = resultsPerBatch;
	}

	public int getPagesBatchIndex() {
		return pagesBatchIndex;
	}

	public void setPagesBatchIndex(int currentBatchIndex) {
		if (currentBatchIndex <= 0)
			throw new IllegalArgumentException("The pages batch index has to be greater than 0");

		this.pagesBatchIndex = currentBatchIndex;
	}
	
	public int getTotalNumberOfPages() {
		return pagesUrls != null ? pagesUrls.size() : 0;
	}
}
