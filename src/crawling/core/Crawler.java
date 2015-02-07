package crawling.core;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Represents a crawler that visits and collects information about web pages
 */
public class Crawler extends WebCrawler {
	public Crawler() {
		pages = new ArrayList<PageProcessingData>(BATCH_INSERT_LIMIT);
	}

	private final static int BATCH_INSERT_LIMIT = 128;
	private List<PageProcessingData> pages;
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|csv|data|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf|pde" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private IPagesRepository repository;

	@Override
	public void onStart() {
		Object data = myController.getCustomData();

		if (!(data instanceof IPagesRepository)) {
			throw new IllegalArgumentException("The web crawler must be supplied with a valid pages repository");
		}

		repository = (IPagesRepository) data;
	}

	@Override
	public void onBeforeExit() {
		if (pages.size() > 1) {
			insertPages();
		}
		
		super.onBeforeExit();
	}
	
	@Override
	protected void onContentFetchError(WebURL webUrl) {
		printMessage("ERROR! Could not fetch " + webUrl.getURL());
		
		super.onContentFetchError(webUrl);
	}
	
	@Override
	protected void onParseError(WebURL webUrl) {
		printMessage("ERROR! Could not parse " + webUrl.getURL());
		
		super.onParseError(webUrl);
	}

	/**
	 * You should implement this function to specify whether the given url should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		return !FILTERS.matcher(href).matches() && href.contains("ics.uci.edu") && !href.contains("?");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by your program.
	 */
	@Override
	public void visit(Page page) {
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

			pages.add(new PageProcessingData(page.getWebURL().getURL(), htmlParseData.getText(), htmlParseData.getHtml()));

			printMessage("Crawled " + page.getWebURL().getURL());

			// If we hit the batch limit, the pages are added to the repository
			if (pages.size() == BATCH_INSERT_LIMIT) {
				insertPages();
			}
		}
	}

	private void insertPages() {
		try {
			repository.insertPages(pages);
		} catch (SQLException e) {
			// TODO: see a better way to throw SQL Exceptions, which are checked exceptions
			throw new RuntimeException(e.getMessage());
		}

		pages.clear();
	}

	
	private void printMessage(String message) {
		String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		System.out.println("[" + currentDateTime + "] - " + message);
	}
}