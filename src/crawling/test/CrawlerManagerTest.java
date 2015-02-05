package crawling.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import org.junit.Before;
import org.junit.Test;

import crawling.core.CrawlParameters;
import crawling.core.Crawler;
import crawling.core.CrawlerManager;
import crawling.core.ICrawlControllerBuilder;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public class CrawlerManagerTest {
	private CrawlParameters parameters;
	private ICrawlControllerBuilder crawlControllerBuilder;
	private CrawlController controller;
	
	@Before
	public final void initialize() throws Exception {
		parameters = mock(CrawlParameters.class);
		crawlControllerBuilder = mock(ICrawlControllerBuilder.class);
		controller = mock(CrawlController.class);
		
		when(parameters.validate()).thenReturn(null);
		when(crawlControllerBuilder.build(parameters)).thenReturn(controller);
	}

	@Test
	public final void testRun() throws Exception {
		CrawlerManager manager = new CrawlerManager();
		
		// Act
		manager.Run(parameters, crawlControllerBuilder, Crawler.class);
		
		// Assert
		verify(crawlControllerBuilder).build(parameters);
		verify(controller).addSeed(parameters.getBaseDomain());
		verify(controller).start(Crawler.class, parameters.getNumberOfCrawlers());
	}

}
