package searchengine.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import searchengine.core.Page;
import searchengine.core.repository.IPagesRepository;
import searchengine.core.repository.MySQLPagesRepository;

// TODO: rewrite this class so that tests are not strongly coupled to the DB as they are for now
// TODO: this needs more tests when mock is available (reset scenario, many batch inserts and reads, clear, etc.)
public class MySQLPagesRepositoryTest {
	private IPagesRepository repository;
	private List<Page> pages;

	@Before
	public final void initialize() throws SQLException, ClassNotFoundException {
		repository = new MySQLPagesRepository();

		pages = new ArrayList<Page>();
		pages.add(new Page("www.testurl1.com", "some text 1", "<html>1</html>"));
		pages.add(new Page("www.testurl2.com", "some text 2", "<html>2</html>"));
		pages.add(new Page("www.testurl3.com", "some text 3", "<html>3</html>"));
	}

	@Test
	public void testRetrieveNextPages() throws SQLException {
		// Arrange
		List<Page> retrievedPages;

		// Act
		repository.insertPages(pages);
		retrievedPages = repository.retrieveNextPages(2);
		repository.deletePages(pages);

		// Assert
		Assert.assertTrue(retrievedPages != null && retrievedPages.size() == 2);
	}
	
	@Test
	public void testRetrievePagesCount() {
		// TODO: Requires implementation
		Assert.fail();
	}

	@Test
	public void testInsertPages() throws SQLException {
		// Arrange
		int[] result;

		// Act
		repository.insertPages(pages);
		result = repository.deletePages(pages);

		// Assert
		Assert.assertTrue(result != null && result.length == pages.size());
	}

	@Test
	public void testUpdatePages() {
		// TODO: Requires implementation
		Assert.fail();
	}

	@Test
	public void testDeletePages() {
		// TODO: Requires implementation
		Assert.fail();
	}

	@Test
	public void testClearPages() {
		// TODO: Requires implementation
		Assert.fail();
	}
}
