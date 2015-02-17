package searchengine.test;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import searchengine.core.IPagesRepository;
import searchengine.core.MySQLPagesRepository;
import searchengine.core.PageProcessingData;

// TODO: rewrite this class so that tests are not strongly coupled to the DB as they are for now
// TODO: this needs more tests (reset scenario, many batch inserts and reads, etc.)
public class MySQLPagesRepositoryTest {
	private IPagesRepository repository;
	PageProcessingData page;

	@Before
	public final void initialize() throws SQLException, ClassNotFoundException {
		repository = new MySQLPagesRepository();

		page = new PageProcessingData("www.testurl1.com", "some text 1", "<html>1</html>");
	}

	@Test
	public void testInsertPages() throws SQLException {
		// Arrange
		int result;

		// Act
		result = repository.insertPage(page);
		repository.clear();

		// Assert
		assertTrue(result == 1);
	}

	@Test
	public void testRetrieveNextPages() throws SQLException {
		// Arrange
		List<PageProcessingData> retrievedPages;

		// Act
		repository.insertPage(page);
		repository.insertPage(new PageProcessingData("www.testurl2.com", "some text 2", "<html>2</html>"));
		repository.insertPage(new PageProcessingData("www.testurl3.com", "some text 3", "<html>3</html>"));
		
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
		repository.insertPage(page);
		result = repository.clear();

		// Assert
		assertTrue(result > 0);
	}

}
