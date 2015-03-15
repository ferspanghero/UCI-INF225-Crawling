package searchengine.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import searchengine.core.repository.IRepositoriesFactory;

/**
 * Represents a basic engine that searches for pages
 */
public class BatchedPagesSearchEngine implements IPagesSearchEngine {
	public BatchedPagesSearchEngine(int pagesPerBatch) {
		setPagesPerBatch(pagesPerBatch);
		setPagesBatchIndex(1);

		searchedPages = null;
		lastSearchedWords = new ArrayList<String>();
	}

	private int pagesPerBatch;
	private int pagesBatchIndex;	
	private List<SearchedPage> searchedPages;
	private List<String> lastSearchedWords;
	private static final int N_GRAM_MAX_DISTANCE = 2;
	private static final int N_GRAM_RANKING_SCORE_WEIGHT = 0;

	@Override
	public List<SearchedPage> search(IRepositoriesFactory repositoriesFactory, String query) throws ClassNotFoundException, SQLException {
		List<SearchedPage> searchedPagesBatch = null;
		Map<Integer, List<Integer>> wordsPagesPositions = null;

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
				searchedPages = repositoriesFactory.getPagesRepository().searchPages(words);
				wordsPagesPositions = repositoriesFactory.getPostingsRepository().retrieveWordsPagesPositions(words);
				
				calculateRankingScoresWithWordPositions(words, wordsPagesPositions);

				lastSearchedWords = words;
			}		
		}
		
		if (searchedPages != null) {
			int startIndex = (getPagesBatchIndex() - 1) * getPagesPerBatch();
			int endIndex = startIndex + getPagesPerBatch();

			searchedPagesBatch = searchedPages.subList(startIndex, Math.min(endIndex, searchedPages.size()));
		}

		return searchedPagesBatch;
	}

	private void calculateRankingScoresWithWordPositions(List<String> words, Map<Integer, List<Integer>> wordsPagesPositions) {
		// Only considers counting n-grams for queries with more than 1 word
		if (wordsPagesPositions != null && searchedPages != null && words != null && words.size() > 1) {
			for (SearchedPage page : searchedPages) {
				if (wordsPagesPositions.containsKey(page.getPageId())) {
					List<Integer> wordsPositions = wordsPagesPositions.get(page.getPageId());
					int nGramCount = 0;
					
					if (wordsPositions != null) {
						for (int i = 0; i < wordsPositions.size() - 1; i++) {
							// We are arbitrarily assuming that words which are distant over N_GRAM_MAX_DISTANCE positions have nothing to do with each other
							if (wordsPositions.get(i + 1) - wordsPositions.get(i) <= N_GRAM_MAX_DISTANCE)
								nGramCount++;
						}
					}
					
					// Includes the found n-grams count to the ranking score. An arbitrary weight is assigned to the n-grams count
					page.setRankingScore(page.getRankingScore() + (nGramCount * N_GRAM_RANKING_SCORE_WEIGHT));
				}
			}
												
			// Sorts again the searched pages list by ranking scores
			Collections.sort(searchedPages, new Comparator<SearchedPage>() {			
				@Override
				public int compare(SearchedPage p1, SearchedPage p2) {
					return Double.compare(p2.getRankingScore(), p1.getRankingScore());
				} 
			});
		}
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
		return searchedPages != null ? searchedPages.size() : 0;
	}
}
