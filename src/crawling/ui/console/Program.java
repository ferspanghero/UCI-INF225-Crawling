package crawling.ui.console;

import crawling.core.CrawlParameters;
import crawling.core.Crawler;
import crawling.core.CrawlerManager;
import crawling.core.DefaultCrawlControllerBuilder;
import crawling.core.ICrawlControllerBuilder;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;

public class Program {

	public static void main(String[] args) {
		CrawlerManager manager = new CrawlerManager();

		try {
			CrawlConfig config = new CrawlConfig();
			ICrawlControllerBuilder crawlControllerBuilder = new DefaultCrawlControllerBuilder();

			config.setCrawlStorageFolder(".\\data\\crawl\\root");
			config.setPolitenessDelay(500);
			config.setUserAgentString("UCI WebCrawler 93082117/30489978/12409858");
			config.setResumableCrawling(true);

			// TODO: see how logger can be injected into the controller
			manager.Run(new CrawlParameters(config, 10, "http://www.ics.uci.edu"), crawlControllerBuilder, Crawler.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
