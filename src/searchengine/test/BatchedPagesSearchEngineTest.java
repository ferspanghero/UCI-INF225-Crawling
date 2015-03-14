package searchengine.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import searchengine.core.BatchedPagesSearchEngine;
import searchengine.core.SearchedPage;
import searchengine.core.repository.IPagesRepository;
import searchengine.core.repository.IPostingsRepository;
import searchengine.core.repository.IRepositoriesFactory;

//TODO: Test edge cases (query is null, etc.)
public class BatchedPagesSearchEngineTest {
	private IRepositoriesFactory repositoriesFactory;
	private List<String> words;
	private List<SearchedPage> searchedPages;
	private Map<Integer, List<Integer>> wordsPagesPositions;
	private static final String SEARCH_QUERY = "search query";

	@Before
	public final void initialize() throws Exception {
		repositoriesFactory = Mockito.mock(IRepositoriesFactory.class);

		searchedPages = new ArrayList<SearchedPage>();
		searchedPages.add(new SearchedPage(1, "URL1", 1.0));
		searchedPages.add(new SearchedPage(2, "URL2", 0.5));

		words = new ArrayList<String>();
		words.add("search");
		words.add("query");

		wordsPagesPositions = new HashMap<Integer, List<Integer>>();
		wordsPagesPositions.put(1, new ArrayList<Integer>());
		wordsPagesPositions.get(1).add(1);
		wordsPagesPositions.get(1).add(2);
		wordsPagesPositions.put(2, new ArrayList<Integer>());
		wordsPagesPositions.get(2).add(1);
		wordsPagesPositions.get(2).add(2);

		Mockito.when(repositoriesFactory.getPagesRepository()).thenReturn(Mockito.mock(IPagesRepository.class));
		Mockito.when(repositoriesFactory.getPostingsRepository()).thenReturn(Mockito.mock(IPostingsRepository.class));
		Mockito.when(repositoriesFactory.getPagesRepository().searchPages(words)).thenReturn(searchedPages);
		Mockito.when(repositoriesFactory.getPostingsRepository().retrieveWordsPagesPositions(words)).thenReturn(wordsPagesPositions);
	}

	@Test
	public void testSearch_ValidQuery() throws ClassNotFoundException, SQLException {
		// Arrange
		BatchedPagesSearchEngine searchEngine = new BatchedPagesSearchEngine(10);
		List<SearchedPage> testSearchedPages;

		// Act
		testSearchedPages = searchEngine.search(repositoriesFactory, SEARCH_QUERY);

		// Assert
		Mockito.verify(repositoriesFactory.getPagesRepository(), Mockito.times(1)).searchPages(words);
		Assert.assertTrue(testSearchedPages.equals(searchedPages));
	}

	@Test
	public void testSearch_ChangePageBatchIndex() throws ClassNotFoundException, SQLException {
		// Arrange
		BatchedPagesSearchEngine searchEngine = new BatchedPagesSearchEngine(1);
		List<SearchedPage> testSearchedPages;

		// Act
		searchEngine.search(repositoriesFactory, SEARCH_QUERY);
		searchEngine.setPagesBatchIndex(2);
		testSearchedPages = searchEngine.search(repositoriesFactory, SEARCH_QUERY);

		// Assert
		Mockito.verify(repositoriesFactory.getPagesRepository(), Mockito.times(1)).searchPages(words);
		Assert.assertTrue(testSearchedPages.size() == 1 && testSearchedPages.get(0).equals(searchedPages.get(1)));
	}
}
