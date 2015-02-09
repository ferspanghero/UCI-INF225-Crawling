package crawling.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import crawling.core.DefaultPagesProcessor;
import crawling.core.IPagesRepository;
import crawling.core.PageProcessingData;
import crawling.core.PagesProcessorConfiguration;

public class DefaultPagesProcessorTest {
	private DefaultPagesProcessor processor;
	private IPagesRepository repository;
	private PagesProcessorConfiguration config;
	private final static int SAMPLE_PAGES_COUNT = 3;
	private final static int MOST_COMMON_COUNT = 2;
	private final static String LONGEST_URL = "http://graphics.ics.uci.edu/about";

	@Before
	public final void initialize() throws SQLException {
		processor = new DefaultPagesProcessor();
		repository = mock(IPagesRepository.class);
		config = getTestPageProcessorConfiguration();

		when(repository.retrieveNextPages(anyInt())).thenReturn(getTestPageProcessingData(), null);
	}

	private PagesProcessorConfiguration getTestPageProcessorConfiguration() {
		HashSet<String> stopWords = new HashSet<String>();

		try (Scanner scanner = new Scanner(DefaultPagesProcessorTest.class.getResourceAsStream("/resources/stopwords.txt"))) {
			while (scanner.hasNextLine()) {
				stopWords.add(scanner.nextLine());
			}
		}

		return new PagesProcessorConfiguration(stopWords, 2, "ics.uci.edu");
	}

	private List<PageProcessingData> getTestPageProcessingData() {
		ArrayList<PageProcessingData> pages = new ArrayList<PageProcessingData>();

		pages.add(new PageProcessingData("http://www.ics.uci.edu/about/equity/", "A sample text 1", "<html>1</html>"));
		pages.add(new PageProcessingData("http://www.ics.uci.edu/about/equity/", "A sample text 2", "<html>1</html>"));
		pages.add(new PageProcessingData("http://graphics.ics.uci.edu/about", "A larger sample here", "<html>1</html>"));

		return pages;
	}

	@Test
	public final void testGetUniquePagesCount() throws SQLException {
		// Arrange
		int uniquePagesCount;

		// Act
		processor.processPages(repository, config);
		uniquePagesCount = processor.getUniquePagesCount();

		// Assert
		assertEquals(uniquePagesCount, SAMPLE_PAGES_COUNT);
	}

	@Test
	public final void testGetSubdomains() throws SQLException {
		// Arrange
		Entry<String, Integer>[] subdomains = (Entry<String, Integer>[]) new Entry[2];

		// Act
		processor.processPages(repository, config);
		subdomains = processor.getSubdomains().entrySet().toArray(subdomains);

		// Assert		
		assertEquals(subdomains[0].getKey(), "http://graphics.ics.uci.edu");
		assertEquals(subdomains[0].getValue(), new Integer(1));
		assertEquals(subdomains[1].getKey(), "http://www.ics.uci.edu");
		assertEquals(subdomains[1].getValue(), new Integer(2));
	}

	@Test
	public final void testGetLongestPage() throws SQLException {
		// Arrange
		String longestPage;

		// Act
		processor.processPages(repository, config);
		longestPage = processor.getLongestPage();

		// Assert
		assertEquals(longestPage, LONGEST_URL);
	}

	@Test
	public final void testGetMostCommonWords() throws SQLException {
		// Arrange
		Entry<String, Integer>[] mostCommonWords = (Entry<String, Integer>[]) new Entry[MOST_COMMON_COUNT];

		// Act
		processor.processPages(repository, config);
		mostCommonWords = processor.getMostCommonWords(MOST_COMMON_COUNT).entrySet().toArray(mostCommonWords);

		// Assert
		assertEquals(mostCommonWords.length, MOST_COMMON_COUNT);
		assertEquals(mostCommonWords[0].getKey(), "sample");
		assertEquals(mostCommonWords[0].getValue(), new Integer(SAMPLE_PAGES_COUNT));
		assertEquals(mostCommonWords[1].getKey(), "text");
		assertEquals(mostCommonWords[1].getValue(), new Integer(SAMPLE_PAGES_COUNT - 1));
	}

	@Test
	public final void testGetMostCommonNGrams() throws SQLException {
		Entry<String, Integer>[] mostCommonNGrams = (Entry<String, Integer>[]) new Entry[MOST_COMMON_COUNT];

		// Act
		processor.processPages(repository, config);
		mostCommonNGrams = processor.getMostCommonNGrams(MOST_COMMON_COUNT).entrySet().toArray(mostCommonNGrams);

		// Assert
		assertEquals(mostCommonNGrams.length, MOST_COMMON_COUNT);
		assertEquals(mostCommonNGrams[0].getKey(), "sample text");
		assertEquals(mostCommonNGrams[0].getValue(), new Integer(SAMPLE_PAGES_COUNT - 1));
	}

}
