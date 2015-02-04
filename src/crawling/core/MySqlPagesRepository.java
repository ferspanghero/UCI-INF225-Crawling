package crawling.core;

import java.util.List;

import edu.uci.ics.crawler4j.crawler.Page;

/**
 * Represents a MySql database that contains data about the crawled pages
 */
public class MySqlPagesRepository implements IPagesRepository {
	@Override
	public void reset() {

	}

	@Override
	public void insertPage(Page page) {
		// TODO Think about bulk insert
	}

	@Override
	public List<PageProcessingData> retrieveNextPages() {
		return null;
	}

	@Override
	public List<PageProcessingData> retrieveNextPages(int pagesChunkSize) {
		return null;
	}
}
