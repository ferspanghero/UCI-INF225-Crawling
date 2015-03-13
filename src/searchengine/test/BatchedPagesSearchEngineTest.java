package searchengine.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import searchengine.core.BatchedPagesSearchEngine;
import searchengine.core.repository.IPagesRepository;
import searchengine.core.repository.IRepositoriesFactory;

//TODO: Test edge cases (query is null, etc.)
public class BatchedPagesSearchEngineTest {
	private IRepositoriesFactory repositoriesFactory;
	private List<String> words;
	private List<String> pagesUrls;
	private static final String SEARCH_QUERY = "search query";

	@Before
	public final void initialize() throws Exception {
		repositoriesFactory = Mockito.mock(IRepositoriesFactory.class);
		pagesUrls = new ArrayList<String>();

		pagesUrls.add("URL1");
		pagesUrls.add("URL2");

		words = new ArrayList<String>();

		words.add("search");
		words.add("query");

		Mockito.when(repositoriesFactory.getPagesRepository()).thenReturn(Mockito.mock(IPagesRepository.class));
		Mockito.when(repositoriesFactory.getPagesRepository().searchPages(words)).thenReturn(pagesUrls);
	}

	@Test
	public void testSearch_ValidQuery() throws ClassNotFoundException, SQLException {
		// Arrange
		BatchedPagesSearchEngine searchEngine = new BatchedPagesSearchEngine(10);
		List<String> searchedPagesUrls;

		// Act
		searchedPagesUrls = searchEngine.search(repositoriesFactory, SEARCH_QUERY);

		// Assert
		Mockito.verify(repositoriesFactory.getPagesRepository(), Mockito.times(1)).searchPages(words);
		Assert.assertTrue(searchedPagesUrls.equals(pagesUrls));
	}

	@Test
	public void testSearch_ChangePageBatchIndex() throws ClassNotFoundException, SQLException {
		// Arrange
		BatchedPagesSearchEngine searchEngine = new BatchedPagesSearchEngine(1);
		List<String> searchedPagesUrls;
		
		// Act
		searchEngine.search(repositoriesFactory, SEARCH_QUERY);
		searchEngine.setPagesBatchIndex(2);
		searchedPagesUrls = searchEngine.search(repositoriesFactory, SEARCH_QUERY);

		// Assert
		Mockito.verify(repositoriesFactory.getPagesRepository(), Mockito.times(1)).searchPages(words);
		Assert.assertTrue(searchedPagesUrls.size() == 1 && searchedPagesUrls.get(0).equals(pagesUrls.get(1)));
	}
}
