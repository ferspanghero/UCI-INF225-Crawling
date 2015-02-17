package searchengine.core;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Represents a crawler that visits and collects information about web pages
 */
public class Crawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|csv|data|java|lif|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|ps|ppt|pdf|pde" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private final static Pattern DOMAIN = Pattern.compile("http://.*\\.ics\\.uci\\.edu.*");
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
		
		if (FILTERS.matcher(href).matches() || href.contains("?") || href.startsWith("http://fano.ics.uci.edu") || href.startsWith("http://ftp.ics.uci.edu"))
			return false;
		else
			return DOMAIN.matcher(href).matches();
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by your program.
	 */
	@Override
	public void visit(Page page) {
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

			PageProcessingData pageProcessingData = new PageProcessingData(page.getWebURL().getURL(), htmlParseData.getText(), htmlParseData.getHtml());

			printMessage("Crawled " + pageProcessingData.getUrl());

			insertPage(pageProcessingData);
		}
	}

	private void insertPage(PageProcessingData page) {
		try {
			repository.insertPage(page);
		} catch (SQLException e) {
			printMessage("Repository error while inserting " + page.getUrl() + ":" + e.getMessage());
		}
	}

	private void printMessage(String message) {
		String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		System.out.println("[" + currentDateTime + "] - " + message);
	}
}