package crawling.test;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import crawling.core.IPagesRepository;
import crawling.core.MySQLPagesRepository;
import crawling.core.PageProcessingData;

// TODO: rewrite this class so that tests are not strongly coupled to the DB as they are for now
// TODO: this needs more tests (reset scenario, many batch inserts and reads, etc.)
public class MySQLPagesRepositoryTest {
	private IPagesRepository repository;
	private List<PageProcessingData> pages;

	@Before
	public final void initialize() throws SQLException, ClassNotFoundException {
		repository = new MySQLPagesRepository();

		pages = new ArrayList<PageProcessingData>();
		pages.add(new PageProcessingData("www.testurl1.com", "some text 1", "<html>1</html>"));
		pages.add(new PageProcessingData("www.testurl2.com", "some text 2", "<html>2</html>"));
		pages.add(new PageProcessingData("www.testurl3.com", "some text 3", "<html>3</html>"));
	}

	@Test
	public void testInsertPages() throws SQLException {
		// Arrange
		int[] result;

		// Act
		repository.insertPages(pages);
		result = repository.deletePages(pages);

		// Assert
		assertTrue(result != null && result.length == pages.size());
	}

	@Test
	public void testRetrieveNextPages() throws SQLException {
		// Arrange
		List<PageProcessingData> retrievedPages;

		// Act
		repository.insertPages(pages);
		retrievedPages = repository.retrieveNextPages(2);
		repository.clear();

		// Assert
		assertTrue(retrievedPages != null && retrievedPages.size() == 2);
	}

	@Test
	public void testClear() throws SQLException {
		// Arrange
		int result;

		// Act
		repository.insertPages(pages);
		result = repository.clear();

		// Assert
		assertTrue(result > 0);
	}

}
