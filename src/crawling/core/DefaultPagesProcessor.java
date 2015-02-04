package crawling.core;

import java.util.HashSet;
import java.util.Map;

/**
 * Represents a basic processor that does a set of operations with the crawled pages
 */
public class DefaultPagesProcessor implements IPagesProcessor {

	public DefaultPagesProcessor(IPagesRepository repository) {
		setRepository(repository);
	}
	
	private IPagesRepository _repository; 
	
	/* (non-Javadoc)
	 * @see crawling.core.IPagesProcessor#setRepository(crawling.core.IPagesRepository)
	 */
	@Override
	public void setRepository(IPagesRepository repository) {
		_repository = repository;
	}

	/* (non-Javadoc)
	 * @see crawling.core.IPagesProcessor#getUniquePagesCount()
	 */
	@Override
	public int getUniquePagesCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see crawling.core.IPagesProcessor#getSubdomains()
	 */
	@Override
	public Map<String, Integer> getSubdomains() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see crawling.core.IPagesProcessor#getLongestPage()
	 */
	@Override
	public String getLongestPage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see crawling.core.IPagesProcessor#getMostCommonWords(int, java.util.HashSet)
	 */
	@Override
	public Map<String, Integer> getMostCommonWords(int count,
			HashSet<String> stopWords) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see crawling.core.IPagesProcessor#getMostCommonNGrams(int, int)
	 */
	@Override
	public Map<String, Integer> getMostCommonNGrams(int count, int n) {
		// TODO Auto-generated method stub
		return null;
	}

}
