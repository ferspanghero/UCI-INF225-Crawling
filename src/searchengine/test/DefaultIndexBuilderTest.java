package searchengine.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import searchengine.core.DefaultIndexBuilder;
import searchengine.core.IndexPosting;
import searchengine.core.repository.IPagesRepository;
import searchengine.core.repository.IPostingsRepository;
import searchengine.core.repository.IRepositoriesFactory;

//TODO: Test edge cases (repository is null, postings are empty, etc.)
public class DefaultIndexBuilderTest {
	private IRepositoriesFactory repositoriesFactory;
	private List<IndexPosting> postings;
	
	@Before
	public final void initialize() throws Exception {
		repositoriesFactory = Mockito.mock(IRepositoriesFactory.class);
		postings = getTestPostings();
		
		Mockito.when(repositoriesFactory.getPagesRepository()).thenReturn(Mockito.mock(IPagesRepository.class));
		Mockito.when(repositoriesFactory.getPostingsRepository()).thenReturn(Mockito.mock(IPostingsRepository.class));
		Mockito.when(repositoriesFactory.getPagesRepository().retrievePagesCount()).thenReturn(2);
		Mockito.when(repositoriesFactory.getPostingsRepository().retrieveNextPostings(Matchers.anyInt())).thenReturn(postings, new ArrayList<IndexPosting>());
		Mockito.when(repositoriesFactory.getPostingsRepository().retrieveWordsPagesFrequencies()).thenReturn(getWordsPagesFrequencies());
	}
	
	private List<IndexPosting> getTestPostings() {
		ArrayList<IndexPosting> postings = new ArrayList<IndexPosting>();

		postings.add(new IndexPosting(1, 1, "word1", 2, 0, null));
		postings.add(new IndexPosting(1, 2, "word2", 5, 0, null));
		postings.add(new IndexPosting(1, 3, "word3", 10, 0, null));
		postings.add(new IndexPosting(2, 1, "word1", 2, 0, null));
		postings.add(new IndexPosting(2, 2, "word2", 1, 0, null));

		return postings;
	}

	private Map<Integer, Integer> getWordsPagesFrequencies() {
		Map<Integer, Integer> wordsPagesFrequencies = new HashMap<Integer, Integer>();
		
		wordsPagesFrequencies.put(1, 2);
		wordsPagesFrequencies.put(2, 2);
		wordsPagesFrequencies.put(3, 1);
		
		return wordsPagesFrequencies;
	}
	
	@Test
	public void testBuidIndex_ExistingPostings() throws ClassNotFoundException, SQLException {
		// Arrange
		DefaultIndexBuilder indexBuilder = new DefaultIndexBuilder(); 
		
		// Act
		indexBuilder.buildIndex(repositoriesFactory);
		
		// Assert
		Mockito.verify(repositoriesFactory.getPostingsRepository(), Mockito.times(1)).reset();
		Mockito.verify(repositoriesFactory.getPagesRepository(), Mockito.times(1)).retrievePagesCount();
		Mockito.verify(repositoriesFactory.getPostingsRepository(), Mockito.times(1)).retrieveWordsPagesFrequencies();
		Mockito.verify(repositoriesFactory.getPostingsRepository(), Mockito.times(2)).retrieveNextPostings(Matchers.anyInt());
		Mockito.verify(repositoriesFactory.getPostingsRepository(), Mockito.times(1)).updatePostings(Matchers.any());
	}
	
	@Test
	public void testBuidIndex_TfIdfCalculation() throws ClassNotFoundException, SQLException {
		// Arrange
		DefaultIndexBuilder indexBuilder = new DefaultIndexBuilder();
		int pagesCount = repositoriesFactory.getPagesRepository().retrievePagesCount();
		Map<Integer, Integer> wordsPagesFrequencies = repositoriesFactory.getPostingsRepository().retrieveWordsPagesFrequencies();
		
		// Act
		indexBuilder.buildIndex(repositoriesFactory);

		// Assert
		for (IndexPosting posting : postings) {
			double calculatedTfIdf = (posting.getWordFrequency() * pagesCount) / wordsPagesFrequencies.get(posting.getWordId());
			
			Assert.assertEquals(posting.getTfIdf(), calculatedTfIdf, 0d);
		}
	}
}
