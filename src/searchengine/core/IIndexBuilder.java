package searchengine.core;

import searchengine.core.repository.IRepositoriesFactory;

/**
 * Represents a builder of the pages terms index
 */
public interface IIndexBuilder {
	/**
	 * Builds the pages term index
	 */
	void buildIndex(IRepositoriesFactory repositoriesFactory);
}
