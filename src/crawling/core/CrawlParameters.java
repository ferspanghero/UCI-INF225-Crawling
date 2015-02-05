package crawling.core;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

/**
 * Represents a set of parameters that configure the web pages crawling
 * operation
 */
public class CrawlParameters {
	public CrawlParameters(CrawlConfig config, int numberOfCrawlers, String baseDomain) {
		setConfig(config);
		setNumberOfCrawlers(numberOfCrawlers);
		setBaseDomain(baseDomain);
	}

	private CrawlConfig config;
	private int numberOfCrawlers;
	private String baseDomain;

	public String validate() {
		StringBuilder errorMessages = new StringBuilder();

		if (getConfig() == null)
			errorMessages.append("Crawling configuration is missing\n");

		if (getNumberOfCrawlers() <= 0)
			errorMessages.append("Number of crawlers cannot be lower than 1\n");

		if (getBaseDomain() == null || getBaseDomain() == "")
			errorMessages.append("Base domain is missing\n");

		return errorMessages.toString();
	}

	/**
	 * @return the config
	 */
	public CrawlConfig getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(CrawlConfig config) {
		this.config = config;
	}

	/**
	 * @return the numberOfCrawlers
	 */
	public int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	/**
	 * @param numberOfCrawlers
	 *            the numberOfCrawlers to set
	 */
	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	/**
	 * @return the baseDomain
	 */
	public String getBaseDomain() {
		return baseDomain;
	}

	/**
	 * @param baseDomain
	 *            the baseDomain to set
	 */
	public void setBaseDomain(String baseDomain) {
		this.baseDomain = baseDomain;
	}	
}
