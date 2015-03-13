package searchengine.core;

import java.sql.SQLException;
import java.util.List;

import searchengine.core.repository.IRepositoriesFactory;

/**
 * Represents an engine that searches for pages
 */
public interface IPagesSearchEngine {
	/**
	 * Searches for pages that match a specific query
	 *
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * 
	 */
	List<String> search(IRepositoriesFactory repositoriesFactory, String query) throws ClassNotFoundException, SQLException;
}
