package crawling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
	private final int SAMPLE_PAGES_COUNT = 5;
	private final String LARGEST_TEXT = "Largest sample text";
	private final int MOST_COMMON_COUNT = 2;

	@Before
	public final void initialize() {
		processor = new DefaultPagesProcessor();
		repository = mock(IPagesRepository.class);
		config = new PagesProcessorConfiguration(null, 2);

		when(repository.retrieveNextPages(anyInt())).thenReturn(getTestPageProcessingData());
	}

	private List<PageProcessingData> getTestPageProcessingData() {
		ArrayList<PageProcessingData> pages = new ArrayList<PageProcessingData>();

		for (int i = 1; i <= SAMPLE_PAGES_COUNT - 1; i++) {
			pages.add(new PageProcessingData("www.testurl" + i + ".com", "A sample text" + i));
		}

		pages.add(new PageProcessingData("www.testurl" + SAMPLE_PAGES_COUNT + ".com", LARGEST_TEXT));

		return pages;
	}

	@Test
	public final void testGetUniquePagesCount() {
		// Arrange
		int uniquePagesCount;

		// Act
		processor.processPages(repository, config);
		uniquePagesCount = processor.getUniquePagesCount();

		// Assert
		assertEquals(uniquePagesCount, SAMPLE_PAGES_COUNT);
	}

	@Test
	public final void testGetSubdomains() {
		fail("Not yet implemented");
	}

	@Test
	public final void testGetLongestPage() {
		// Arrange
		String longestPage;

		// Act
		processor.processPages(repository, config);
		longestPage = processor.getLongestPage();

		// Assert
		assertEquals(longestPage, LARGEST_TEXT);
	}

	@Test
	public final void testGetMostCommonWords() {
		// Arrange
		Entry<String, Integer>[] mostCommonWords = (Entry<String, Integer>[]) new Entry[MOST_COMMON_COUNT];

		// Act
		processor.processPages(repository, config);
		mostCommonWords = processor.getMostCommonWords(MOST_COMMON_COUNT).entrySet().toArray(mostCommonWords);

		// Assert
		assertEquals(mostCommonWords.length, MOST_COMMON_COUNT);
		assertEquals(mostCommonWords[0].getKey(), "sample");
		assertEquals(mostCommonWords[0].getValue(), new Integer(SAMPLE_PAGES_COUNT));
		assertEquals(mostCommonWords[1].getKey(), "a");
		assertEquals(mostCommonWords[1].getValue(), new Integer(SAMPLE_PAGES_COUNT - 1));
	}

	@Test
	public final void testGetMostCommonNGrams() {
		Entry<String, Integer>[] mostCommonNGrams = (Entry<String, Integer>[]) new Entry[MOST_COMMON_COUNT];

		// Act
		processor.processPages(repository, config);
		mostCommonNGrams = processor.getMostCommonNGrams(MOST_COMMON_COUNT).entrySet().toArray(mostCommonNGrams);

		// Assert
		assertEquals(mostCommonNGrams.length, MOST_COMMON_COUNT);
		assertEquals(mostCommonNGrams[0].getKey(), "a sample");
		assertEquals(mostCommonNGrams[0].getValue(), new Integer(SAMPLE_PAGES_COUNT - 1));
	}

}
